package studio.crud.feature.jpa.util
import org.hibernate.Session
import javax.persistence.EntityManager

/**
 * Unwrap to hibernate's [Session]
 */
val EntityManager.hibernateSession get() = unwrap(Session::class.java)