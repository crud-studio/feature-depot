package studio.crud.feature.auth.authentication.rules

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.filter
import com.antelopesystem.crudframework.utils.component.componentmap.annotation.ComponentMap
import studio.crud.feature.auth.entity.model.Entity
import studio.crud.feature.auth.authentication.rules.action.base.RuleActionHandler
import studio.crud.feature.auth.authentication.rules.anomaly.base.DeviceAnomalyHandler
import studio.crud.feature.core.audit.RequestSecurityMetadata
import org.springframework.stereotype.Component

@Component
class AuthenticationValidatorImpl(
        private val crudHandler: CrudHandler
) : AuthenticationValidator {

    @ComponentMap
    private lateinit var actionHandlers: Map<RuleActionType, RuleActionHandler>

    @ComponentMap
    private lateinit var anomalyHandlers: Map<DeviceAnomalyType, DeviceAnomalyHandler>

    override fun validate(entity: Entity, securityMetadata: RequestSecurityMetadata): Int {
        val score = computeScore(entity, securityMetadata)
        val ruleRanges = getRuleRanges()

        for ((rule, range) in ruleRanges) {
            if (range.contains(score)) {
                actionHandlers[rule.action]?.handle(entity, securityMetadata)
            }
        }

        return score
    }

    private fun computeScore(entity: Entity, securityMetadata: RequestSecurityMetadata) : Int {
        var points = 0
        for ((type, handler) in anomalyHandlers) {
            val result = handler.handle(entity, securityMetadata)
            if (result) {
                points += type.points
            }
        }

        return points
    }

    private fun getRuleRanges() : Map<AuthenticationRule, IntRange> {
        val loginRules = crudHandler.index(filter {
            order {
                by = "minScore"
                ascending
            }
        }, AuthenticationRule::class.java)
                .fromCache()
                .execute()
                .data
        if (loginRules.isEmpty()) {
            return mutableMapOf()
        }

        val ruleRanges = mutableMapOf<AuthenticationRule, IntRange>()

        for ((i, loginRule) in loginRules.withIndex()) {
            val nextThreshold = loginRules.getOrNull(i + 1)?.minScore?.minus(1) ?: Int.MAX_VALUE
            ruleRanges[loginRule] = loginRule.minScore..nextThreshold
        }

        return ruleRanges
    }
}

