package studio.crud.feature.dashboard.widget

import org.springframework.stereotype.Repository
import studio.crud.sharedcommon.utils.hibernateSession
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class WidgetHelperDaoImpl(
    @PersistenceContext
    private val em: EntityManager
) : WidgetHelperDao {
    private val session = em.hibernateSession
    override fun runNativeQuery(queryString: String): List<Any> {
        val query = session.createNativeQuery(queryString)
        return query.resultList
    }
}