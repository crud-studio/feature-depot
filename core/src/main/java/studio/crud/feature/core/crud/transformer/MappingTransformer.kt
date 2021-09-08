package studio.crud.feature.core.crud.transformer

import com.antelopesystem.crudframework.crud.handler.CrudHandler
import com.antelopesystem.crudframework.fieldmapper.transformer.base.FieldTransformerBase
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