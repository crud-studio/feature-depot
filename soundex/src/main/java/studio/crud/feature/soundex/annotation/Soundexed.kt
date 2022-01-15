package studio.crud.feature.soundex.annotation

import studio.crud.crudframework.crud.annotation.WithHooks
import studio.crud.feature.soundex.SoundexPersistentHooks

/**
 * Add soundex capabilities to the given entity
 */
@Target(AnnotationTarget.CLASS)
@WithHooks(hooks = [SoundexPersistentHooks::class])
annotation class Soundexed

