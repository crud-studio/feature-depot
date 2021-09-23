package studio.crud.feature.soundex.doublemetaphone.transliterator

interface LanguageTransliterator {

    val language: TransliterationLanguage

    fun matches(char: Char): Boolean = false

    fun transliterate(input: String): String
}