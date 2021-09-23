package studio.crud.feature.soundex.annotation

/**
 * Generate soundex for a given column name
 */
@Target(AnnotationTarget.FIELD)
annotation class SoundexFor(val value: String)