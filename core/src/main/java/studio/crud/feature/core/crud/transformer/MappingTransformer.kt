package studio.crud.feature.core.crud.transformer

import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.fieldmapper.transformer.base.FieldTransformerBase
import org.springframework.stereotype.Component
import java.lang.reflect.Field

@Component("mappingTransformer")
class MappingTransformer(
        private val crudHandler: CrudHandler
) : FieldTransformerBase<Any, Any>() {
    override fun innerTransform(fromField: Field, toField: Field, originalValue: Any?, fromObject: Any, toObject: Any): Any? {
        originalValue ?: return null
        return crudHandler.fill(originalValue, toField.type)
    }
}