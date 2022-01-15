package studio.crud.feature.remotestorage

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.feature.remotestorage.ro.RemoteStorageValueDTO

interface RemoteStorageService {
    fun getValues(filter: DynamicModelFilter): List<RemoteStorageValueDTO>
    fun getValue(identifier: String): RemoteStorageValueDTO
    fun setValue(identifier: String, value: String): RemoteStorageValueDTO
    fun deleteValue(identifier: String)
}