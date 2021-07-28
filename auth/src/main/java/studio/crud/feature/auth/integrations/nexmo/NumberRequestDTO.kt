package studio.crud.feature.auth.integrations.nexmo

data class NumberRequestDTO(val requestId: String, val timeOfRequest: Long = System.currentTimeMillis())