package studio.crud.feature.notes.resolver

import studio.crud.feature.notes.ro.NoteEntityInfoRO

object DefaultNoteEntityResolver : NoteEntityResolver {
    override fun getInfo(entityType: String, entityId: String): NoteEntityInfoRO {
        return NoteEntityInfoRO.UNKNOWN
    }
}