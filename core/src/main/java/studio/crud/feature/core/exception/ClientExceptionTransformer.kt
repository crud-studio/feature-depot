package studio.crud.feature.core.exception

import studio.crud.feature.core.web.model.ResultRO

interface ClientExceptionTransformer {
    fun processExceptionForClient(e: Exception): ResultRO<*>
}