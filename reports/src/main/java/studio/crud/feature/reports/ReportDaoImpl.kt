package studio.crud.feature.reports

import org.hibernate.SQLQuery
import org.hibernate.Session
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Class for DAO with trading entities.
 */
@Repository
class ReportDaoImpl(
    @PersistenceContext
    private val em: EntityManager
) : ReportDao {
    private val session get() = em.unwrap(Session::class.java)
    /**
     * {@inheritDoc}
     */
    override fun runStoredSqlReturnMultiColumn(sql: String, parameters: Map<String, Array<out Any>>, maxResults: Int?): List<Array<Any>> {
        val query: SQLQuery<*> = session
            .createSQLQuery(sql)
        for ((key, value) in parameters) {
            if (value.size == 1) {
                query.setParameter(key, value[0])
            } else if (value.size > 1) {
                query.setParameterList(key, value)
            }
        }
        if (maxResults != null) {
            query.maxResults = maxResults
        }
        return query.list() as List<Array<Any>>
    }
}