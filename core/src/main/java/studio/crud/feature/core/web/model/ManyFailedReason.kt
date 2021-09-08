package studio.crud.feature.core.web.model

data class ManyFailedReason<FailedObject>(
        val `object`: FailedObject,
        val reason: String
)
