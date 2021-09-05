package studio.crud.feature.auth.integrations.nexmo

/**
 * Nexmo code length to use
 */
enum class NexmoCodeLength(val value: Int) {
    SHORT(4),
    LONG(6)
}