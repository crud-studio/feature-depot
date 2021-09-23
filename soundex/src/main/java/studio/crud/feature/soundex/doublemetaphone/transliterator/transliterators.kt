    package studio.crud.feature.soundex.doublemetaphone.transliterator

import studio.crud.feature.soundex.doublemetaphone.exception.TransliteratorNotFoundException

val transliterators = mapOf(
    TransliterationLanguage.English to englishTransliterator,
    TransliterationLanguage.Hebrew to hebrewTransliterator
)

fun transliterate(input: String, language: TransliterationLanguage? = null) : String {

    return transliterators[language ?: detectLanguage(input)]?.transliterate(input) ?: throw TransliteratorNotFoundException(language)
}

private fun detectLanguage(input: String): TransliterationLanguage {
    if(!input.isBlank()) {
        val firstChar = input[0]
        for ((_, transliterator) in transliterators) {
            if(transliterator.matches(firstChar)) {
                return transliterator.language
            }
        }
    }

    return TransliterationLanguage.English
}

