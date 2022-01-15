package studio.crud.feature.auth.authentication.mfa

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMap
import org.springframework.stereotype.Service
import studio.crud.feature.auth.authentication.mfa.enums.MfaType
import studio.crud.feature.auth.authentication.mfa.model.MfaAvailableProviderDTO
import studio.crud.feature.auth.authentication.mfa.model.MfaTypePairPojo
import studio.crud.feature.auth.authentication.mfa.provider.base.MfaProvider
import studio.crud.feature.auth.authentication.model.CustomParamsDTO
import studio.crud.feature.auth.config.properties.AuthProperties
import studio.crud.feature.auth.entity.EntityHandler
import studio.crud.feature.auth.entity.model.EntityMfaMethod
import studio.crud.feature.auth.exception.MfaProviderAlreadyConfiguredForEntityException
import studio.crud.feature.auth.exception.MfaProviderNotConfiguredForEntityException
import studio.crud.feature.auth.exception.MfaProviderNotSupportedException
import studio.crud.feature.auth.exception.NoMfaSetupInProgressException
import studio.crud.feature.auth.model.UserInfo
import studio.crud.feature.auth.token.TokenHandler
import studio.crud.feature.core.util.extractAndValidatePayload

@Service
class MfaServiceImpl(private val crudHandler: CrudHandler,
                     private val entityHandler: EntityHandler,
                     private val tokenHandler: TokenHandler,
                     private val authProperties: AuthProperties
) : MfaService {

    private val setupMfas = mutableMapOf<MfaTypePairPojo, CustomParamsDTO>()

    @ComponentMap
    private lateinit var mfaProviders: Map<MfaType, MfaProvider<Any>>

    override fun setup(mfaType: MfaType, body: String, userInfo: UserInfo): CustomParamsDTO {
        val provider = getMfaProvider(mfaType)
        val payload = extractAndValidatePayload(body, provider.payloadType)
        val entity = entityHandler.getEntityById(userInfo.internalId)
        val params = provider.setup(payload, entity)
        setupMfas[MfaTypePairPojo(entity.id, mfaType)] = params
        return params
    }

    override fun activate(mfaType: MfaType, code: String, userInfo: UserInfo) {
        val existingMethod = getMfaMethod(userInfo.internalId, mfaType)
        if (existingMethod != null) {
            throw MfaProviderAlreadyConfiguredForEntityException(mfaType)
        }

        val pendingParams = setupMfas[MfaTypePairPojo(userInfo.internalId, mfaType)] ?: throw NoMfaSetupInProgressException(mfaType)
        val provider = getMfaProvider(mfaType)
        val entity = entityHandler.getEntityById(userInfo.internalId)
        provider.validate(code, entity, pendingParams)
        entity.mfaMethods.add(
            EntityMfaMethod(
                entity,
                mfaType,
                pendingParams
        )
        )
        crudHandler.update(entity).execute()
    }

    override fun deactivate(mfaType: MfaType, userInfo: UserInfo) {
        val existingMethod = getMfaMethodOrThrow(userInfo.internalId, mfaType)
        crudHandler.delete(existingMethod.id, EntityMfaMethod::class.java).execute()
    }

    override fun issue(mfaType: MfaType, userInfo: UserInfo) {
        val entity = entityHandler.getEntityById(userInfo.internalId)
        val methodHandler = getMfaProvider(mfaType)
        val pendingParams = setupMfas[MfaTypePairPojo(userInfo.internalId, mfaType)]
        if(pendingParams != null) {
            methodHandler.issue(entity, pendingParams)
            return
        }
        val existingMethod = getMfaMethodOrThrow(userInfo.internalId, mfaType)
        methodHandler.issue(entity, crudHandler.fill(existingMethod, CustomParamsDTO::class.java))
    }

    override fun validateCurrentToken(mfaType: MfaType, code: String, userInfo: UserInfo): String {
        val existingMethod = getMfaMethodOrThrow(userInfo.internalId, mfaType)
        val entity = entityHandler.getEntityById(userInfo.internalId)
        val methodHandler = getMfaProvider(mfaType)
        methodHandler.validate(code, entity, crudHandler.fill(existingMethod, CustomParamsDTO::class.java))
        val parsedToken = userInfo.parsedToken
        if(parsedToken.payload.passwordChangeRequired) {
            val newPayload = parsedToken.payload.copy(mfaRequired = false)
            return tokenHandler.generateToken(newPayload).token
        }

        return userInfo.parsedToken.token
    }

    override fun getAvailableProviders(userInfo: UserInfo): List<MfaAvailableProviderDTO> {
        val entity = entityHandler.getEntityById(userInfo.internalId)
        return mfaProviders.map { (type, _) -> MfaAvailableProviderDTO(type, entity.mfaMethods.any { it.type == type })  }
    }

    override fun getEnabledProviders(userInfo: UserInfo): List<MfaType> {
        val entity = entityHandler.getEntityById(userInfo.internalId)
        return entity.mfaMethods.map { it.type }
    }

    private fun getMfaMethod(entityId: Long, mfaType: MfaType): EntityMfaMethod? {
        val entity = entityHandler.getEntityById(entityId)
        return entity.mfaMethods.find { it.type == mfaType }
    }

    private fun getMfaMethodOrThrow(entityId: Long, mfaType: MfaType): EntityMfaMethod {
        val entity = entityHandler.getEntityById(entityId)
        return getMfaMethod(entityId, mfaType) ?: throw MfaProviderNotConfiguredForEntityException(mfaType)
    }

    private fun getMfaProvider(mfaType: MfaType): MfaProvider<Any> {
        val provider = mfaProviders[mfaType] ?: throw MfaProviderNotSupportedException(mfaType)
        return provider
    }
}

