package studio.crud.feature.soundex.transliterator

object hebrewTransliterator : LanguageTransliterator {
    private val replaceMap: Map<String, String?> = mapOf(
        "א" to "a",
        "ב" to "b",
        "ג" to "g",
        "ד" to "d",
        "ה" to "h",
        "ו" to "v",
        "ז" to "z",
        "ח" to "ch",
        "ט" to "t",
        "י" to "y",
        "ך" to "k",
        "כ" to "k",
        "ל" to "l",
        "ם" to "m",
        "מ" to "m",
        "ן" to "n",
        "נ" to "n",
        "ס" to "s",
        "ע" to "gh",
        "ף" to "p",
        "פ" to "p",
        "ץ" to "tz",
        "צ" to "tz",
        "ק" to "q",
        "ר" to "r",
        "ש" to "sh",
        "ת" to "t"
    )

    override fun matches(char: Char): Boolean {
        return Regex("\\p{InHebrew}").matches(char.toString())
    }

    override val language: TransliterationLanguage
        get() = TransliterationLanguage.Hebrew

    override fun transliterate(input: String): String {
        var output = ""
        input.forEachIndexed { i, char ->
            output += replaceMap[char.toString()] ?: char
        }
        return output
    }
}

