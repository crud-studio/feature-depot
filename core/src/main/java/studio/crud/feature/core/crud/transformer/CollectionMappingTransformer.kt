@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package studio.crud.feature.core.crud.transformer

import org.springframework.stereotype.Component
import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.fieldmapper.transformer.base.FieldTransformerBase
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import java.lang.reflect.Field

@Component("collectionMappingTransformer")
class CollectionMappingTransformer(
    private val crudHandler: CrudHandler
) : FieldTransformerBase<Collection<Any>, Collection<Any>>() {
    override fun innerTransform(fromField: Field, toField: Field, originalValue: Collection<Any>?, fromObject: Any, toObject: Any): Collection<Any>? {
        originalValue ?: return null
        val type = (toField.genericType as ParameterizedTypeImpl).actualTypeArguments[0]
        val baseMapping = originalValue.map { crudHandler.fill(it, type as Class<Any>?) }

        if (Set::class.java.isAssignableFrom(toField.type)) {
            return baseMapping.toSet()
        }
        return baseMapping
    }
}
