package studio.crud.feature.tracelog

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.modelfilter.dsl.where
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import studio.crud.feature.tracelog.model.TraceLog
import studio.crud.feature.tracelog.model.TraceRequest
import java.util.*

@Component
class TraceLogHandlerImpl : TraceLogHandler {

    @Autowired
    lateinit var crudHandler: CrudHandler

    override fun getActiveTraces(): List<TraceRequest> {
        return crudHandler.index(where { "expiry" GreaterThan Date() }, TraceRequest::class.java).execute().data
    }

    @Transactional(readOnly = false)
    override fun saveTraces(traces: List<TraceLog>) {
        traces.forEach {
            crudHandler.create(it).execute()
        }
    }
}