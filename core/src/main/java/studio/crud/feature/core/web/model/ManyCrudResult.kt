package studio.crud.feature.core.web.model

data class ManyCrudResult<SuccessfulResult, FailedResult>(
        val successful: Set<SuccessfulResult>,
        val failed: List<ManyFailedReason<FailedResult>>
)
