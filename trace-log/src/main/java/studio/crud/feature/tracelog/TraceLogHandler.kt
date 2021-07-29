package studio.crud.feature.tracelog

import studio.crud.feature.tracelog.model.TraceLog
import studio.crud.feature.tracelog.model.TraceRequest

interface TraceLogHandler {
    fun getActiveTraces(): List<TraceRequest>
    fun saveTraces(traces: List<TraceLog>)
}