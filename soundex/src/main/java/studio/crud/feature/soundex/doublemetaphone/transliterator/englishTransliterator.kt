package studio.crud.feature.soundex.doublemetaphone.transliterator

object englishTransliterator : LanguageTransliterator {
    override val language: TransliterationLanguage
        get() = TransliterationLanguage.English

    override fun transliterate(input: String): String {
        return input
    }
}

