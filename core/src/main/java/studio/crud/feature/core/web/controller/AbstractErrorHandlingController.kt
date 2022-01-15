package studio.crud.feature.core.web.controller

import studio.crud.crudframework.ro.PagingDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import studio.crud.feature.core.audit.RequestSecurityMetadataProvider
import studio.crud.feature.core.exception.ClientExceptionTransformer
import studio.crud.feature.core.web.model.ResultRO

abstract class AbstractErrorHandlingController {
    @Autowired
    protected lateinit var securityMetadataProvider: RequestSecurityMetadataProvider

    @Autowired
    private lateinit var clientExceptionTransformer: ClientExceptionTransformer

    fun wrapResult(resultProvider: () -> Any?): ResponseEntity<ResultRO<*>> {
        val requestMetadata = securityMetadataProvider.get()
        val resultRO = ResultRO<Any?>()
        try {
            val result = resultProvider()
            if (result is ResultRO<*>) {
                result.requestId = requestMetadata.requestId
                return ResponseEntity.ok(resultRO)
            }
            resultRO.requestId = requestMetadata.requestId
            if (result is PagingDTO<*>) {
                resultRO.result = result.data
                resultRO.paging = result.pagingRO
            } else {
                resultRO.result = result
            }
        } catch (e: Exception) {
            return ResponseEntity.ok(clientExceptionTransformer.processExceptionForClient(e))
        }

        return ResponseEntity.ok(resultRO)
    }
}