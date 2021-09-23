import studio.crud.feature.core.exception.ServerException
import studio.crud.feature.core.exception.annotation.ExceptionMetadata

@ExceptionMetadata
class CannotSearchSoundexNonContainsException : ServerException(
    "Operation of soundex query must be contains"
)

@ExceptionMetadata
class SoundexSearchValueMustNotBeEmptyException : ServerException(
    "Value of soundex query cannot be null or empty"
)

@ExceptionMetadata
class SoundexSearchValueMustBeString : ServerException(
    "Value of soundex query must be a string"
)