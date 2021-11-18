package studio.crud.feature.notes.resolver

import studio.crud.feature.notes.ro.NoteEntityInfoRO

interface NoteEntityResolver {
    fun getInfo(entityType: String, entityId: String) : NoteEntityInfoRO
}