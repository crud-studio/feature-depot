package studio.crud.feature.core.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class DateLongFormatTypeAdapter : TypeAdapter<Date?>() {
    override fun write(out: JsonWriter, value: Date?) {
        if (value != null) {
            out.value(value.time)
        } else {
            out.nullValue()
        }
    }

    override fun read(`in`: JsonReader): Date? {
        return if (!`in`.hasNext()) {
            null
        } else {
            Date(`in`.nextLong())
        }
    }
}