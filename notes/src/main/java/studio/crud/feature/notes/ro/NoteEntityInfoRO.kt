package studio.crud.feature.notes.ro

class NoteEntityInfoRO(
    var displayName: String
) {
    companion object {
        val UNKNOWN = NoteEntityInfoRO("N/A")
    }
}