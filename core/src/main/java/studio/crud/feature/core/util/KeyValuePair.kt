package studio.crud.feature.core.util

data class KeyValuePair<K, V>(val key: K, val value: V) {
    companion object {
        fun <K, V> Map<K, V>.toKeyValuePairs(): List<KeyValuePair<K, V>> {
            return this.entries.map { KeyValuePair(it.key, it.value) }
        }

        fun <K, V> Collection<KeyValuePair<K, V>>.toMap(): Map<K, V> {
            return this.associate { it.key to it.value }
        }

        fun KeyValuePair<*, *>.toPrettyString(): String {
            return "${this.key}: ${this.value}"
        }

        infix fun <K, V> K.vs(value: V): KeyValuePair<K, V> {
            return KeyValuePair(this, value)
        }
    }
}