package studio.crud.feature.soundex.doublemetaphone

import org.apache.commons.lang3.StringUtils
import studio.crud.feature.soundex.transliterator.TransliterationLanguage
import studio.crud.feature.soundex.transliterator.transliterate

private const val DEFAULT_MAX_LENGTH = 16
private val dm = DoubleMetaphoneAlgo()


fun List<String>.mapSoundex(language: TransliterationLanguage? = null, maxLength: Int = DEFAULT_MAX_LENGTH) : List<String> {
    if(this.isEmpty()) {
        return emptyList()
    }

    val output = mutableListOf<String>()
    for (input in this) {
        val result = input.toSoundex(language, maxLength)
        if(result.isNotBlank()) {
            output += result
        }
    }
    return output
}

fun String.toSoundex(language: TransliterationLanguage? = null, maxLength: Int = DEFAULT_MAX_LENGTH) : String {
    val cleanedInput = this.replace("[^\\p{L}^\\p{N}\\s%]+".toRegex(), "").trim()
    if(cleanedInput.isBlank()) {
        return ""
    }
    return StringUtils.left(dm.doubleMetaphone(transliterate(cleanedInput, language)), maxLength)
}