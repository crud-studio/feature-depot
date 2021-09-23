package studio.crud.feature.soundex.transliterator

object englishTransliterator : LanguageTransliterator {
    override val language: TransliterationLanguage
        get() = TransliterationLanguage.English

    override fun transliterate(input: String): String {
        return input
    }
}

