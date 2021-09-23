package studio.crud.feature.soundex

import CannotSearchSoundexNonContainsException
import SoundexSearchValueMustBeString
import SoundexSearchValueMustNotBeEmptyException
import com.antelopesystem.crudframework.crud.hooks.interfaces.*
import com.antelopesystem.crudframework.model.BaseCrudEntity
import com.antelopesystem.crudframework.modelfilter.DynamicModelFilter
import com.antelopesystem.crudframework.modelfilter.FilterField
import com.antelopesystem.crudframework.modelfilter.dsl.and
import com.antelopesystem.crudframework.modelfilter.dsl.or
import com.antelopesystem.crudframework.modelfilter.enums.FilterFieldOperation
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import studio.crud.feature.core.exception.InternalException
import studio.crud.feature.soundex.annotation.SoundexFor
import studio.crud.feature.soundex.doublemetaphone.mapSoundex
import java.io.Serializable
import kotlin.reflect.jvm.jvmName

@Component
class SoundexPersistentHooks :
    UpdateHooks<Serializable, BaseCrudEntity<Serializable>>,
    UpdateFromHooks<Serializable, BaseCrudEntity<Serializable>>,
    CreateHooks<Serializable, BaseCrudEntity<Serializable>>,
    CreateFromHooks<Serializable, BaseCrudEntity<Serializable>>,
    IndexHooks<Serializable, BaseCrudEntity<Serializable>>,
    ShowByHooks<Serializable, BaseCrudEntity<Serializable>>
{
    override fun preIndex(filter: DynamicModelFilter) {
        for (filterField in filter.filterFields) {
            decorateSoundexFilter(filterField)
        }
    }

    override fun preShowBy(filter: DynamicModelFilter) {
        for (filterField in filter.filterFields) {
            decorateSoundexFilter(filterField)
        }
    }

    override fun onUpdate(entity: BaseCrudEntity<Serializable>) {
        handleSoundex(entity)
    }

    override fun onUpdateFrom(entity: BaseCrudEntity<Serializable>, ro: Any) {
        handleSoundex(entity)
    }

    override fun onCreate(entity: BaseCrudEntity<Serializable>) {
        handleSoundex(entity)
    }

    override fun onCreateFrom(entity: BaseCrudEntity<Serializable>, ro: Any) {
        handleSoundex(entity)
    }

    private fun handleSoundex(entity: BaseCrudEntity<Serializable>) {
        ReflectionUtils.doWithFields(entity::class.java) {
            val annotation = it.getDeclaredAnnotation(SoundexFor::class.java) ?: return@doWithFields
            val field = ReflectionUtils.findField(entity::class.java, annotation.value) ?: throw InternalException("Field ${annotation.value} not found on class ${entity::class.jvmName}")
            ReflectionUtils.makeAccessible(field)
            val sourceValue = field.get(entity) ?: return@doWithFields
            if(sourceValue.toString().isNotBlank()) {
                val processedValue = sourceValue.toString().split(" ")
                    .mapSoundex()
                    .joinToString("_")
                ReflectionUtils.makeAccessible(it)
                it.set(entity, processedValue)
            }
        }
    }

    private fun decorateSoundexFilter(filterField: FilterField) {
        if(filterField.fieldName != null && filterField.fieldName.contains("Soundex")) {
            val fieldName = filterField.fieldName.replace("Soundex", "")
            val originalOperation = filterField.operation;
            if(originalOperation != FilterFieldOperation.Contains) {
                throw CannotSearchSoundexNonContainsException()
            }
            val value = filterField.value1 ?: SoundexSearchValueMustNotBeEmptyException()
            if(value !is String) {
                throw SoundexSearchValueMustBeString()
            }
            if(value.isEmpty()) {
                throw SoundexSearchValueMustNotBeEmptyException()
            }
            filterField.fieldName = null
            filterField.values = null
            filterField.operation = FilterFieldOperation.Or
            val soundexQuery = value
                .trim()
                .split(" ")
                .filter { it.isNotEmpty() }
                .mapSoundex()
            val soundexCondition = and {
                for (subQuery in soundexQuery) {
                    if (subQuery.length >= 4) {
                        "${fieldName}Soundex" Contains "_${subQuery}"
                    }
                }
            }

            val condition = or {
                fieldName Contains value
                if (soundexCondition.children.isNotEmpty()) {
                    add(soundexCondition)
                }
            }
            filterField.children = listOf(condition)
        } else if (!filterField.children.isNullOrEmpty()) {
            for (child in filterField.children) {
                decorateSoundexFilter(child)
            }
        }
    }
}