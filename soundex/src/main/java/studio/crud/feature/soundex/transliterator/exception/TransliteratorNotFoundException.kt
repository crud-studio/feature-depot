
package studio.crud.feature.soundex.transliterator.exception

import studio.crud.feature.core.exception.annotation.ExceptionMetadata
import studio.crud.feature.core.exception.annotation.ExceptionParam
import studio.crud.feature.soundex.transliterator.TransliterationLanguage
import java.rmi.ServerException

@ExceptionMetadata(
    params = [
        ExceptionParam("language")
    ]
)
class TransliteratorNotFoundException(val language: TransliterationLanguage?) : ServerException(
    "No transliterator found for $language"
)