package studio.crud.feature.auth.integrations.nexmo

data class NexmoBypassNumberPojo(
        var telephone: String = "",
        var code: String = ""
)