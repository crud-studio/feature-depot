package studio.crud.feature.remotestorage

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.DynamicModelFilter
import com.antelopesystem.crudframework.modelfilter.dsl.and
import com.antelopesystem.crudframework.modelfilter.dsl.where
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import studio.crud.feature.core.audit.util.CurrentEntityResolver
import studio.crud.feature.core.util.createOrUpdate
import studio.crud.feature.remotestorage.exceptions.CouldNotDetermineValueOwnerException
import studio.crud.feature.remotestorage.exceptions.RemoteStorageValueNotFoundByIdentifierException
import studio.crud.feature.remotestorage.model.RemoteStorageValue
import studio.crud.feature.remotestorage.ro.RemoteStorageValueDTO

@Service
class RemoteStorageServiceImpl(
    @Autowired
    private val currentEntityResolver: CurrentEntityResolver,
    private val crudHandler: CrudHandler
): RemoteStorageService {
    override fun getValues(filter: DynamicModelFilter): List<RemoteStorageValueDTO> {
        filter.limit = SEARCH_LIMIT
        return crudHandler
            .index(filter.withOwnerFilter(), RemoteStorageValue::class.java, RemoteStorageValueDTO::class.java)
            .execute()
            .data
    }

    override fun getValue(identifier: String): RemoteStorageValueDTO {
        return crudHandler.fill(getValueInternal(identifier), RemoteStorageValueDTO::class.java)
    }

    override fun setValue(identifier: String, value: String): RemoteStorageValueDTO {
        val remoteStorageValue = crudHandler.createOrUpdate<RemoteStorageValue>(
            where {
                "identifier" Equal identifier
            }.withOwnerFilter(),
            {
                it.value = value
            },
            {
                val owner = getOwner()
                RemoteStorageValue(identifier, value, owner.objectId, owner.objectType)
            })
            .execute()
        return crudHandler.fill(remoteStorageValue, RemoteStorageValueDTO::class.java)
    }

    override fun deleteValue(identifier: String) {
        val value = getValueInternal(identifier)
        crudHandler.delete(value.id, RemoteStorageValue::class.java).execute()
    }

    private fun getValueInternal(identifier: String): RemoteStorageValue {
        return crudHandler.showBy(where {
            "identifier" Equal identifier
        }.withOwnerFilter(), RemoteStorageValue::class.java)
            .execute() ?: throw RemoteStorageValueNotFoundByIdentifierException(identifier)
    }


    private fun DynamicModelFilter.withOwnerFilter(): DynamicModelFilter {
        val owner = getOwner()
        this.add(and {
            "ownerId" Equal owner.objectId
            "ownerType" Equal owner.objectType
        })
        return this
    }


    private fun getOwner(): CurrentEntityResolver.ResolvedEntity {
        return currentEntityResolver.resolve() ?: throw CouldNotDetermineValueOwnerException()
    }

    companion object {
        private const val SEARCH_LIMIT = 1000
    }
}