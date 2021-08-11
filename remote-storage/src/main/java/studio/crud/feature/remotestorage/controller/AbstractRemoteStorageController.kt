package studio.crud.feature.remotestorage.controller

import com.antelopesystem.crudframework.modelfilter.DynamicModelFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import studio.crud.feature.remotestorage.RemoteStorageService
import studio.crud.sharedcommon.web.controller.AbstractErrorHandlingController
import studio.crud.sharedcommon.web.ro.ResultRO

abstract class AbstractRemoteStorageController : AbstractErrorHandlingController() {
    @Autowired
    private lateinit var remoteStorageService: RemoteStorageService

    @PostMapping("/getValues")
    fun getValues(@RequestBody filter: DynamicModelFilter): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            remoteStorageService.getValues(filter)
        }
    }

    @GetMapping("/getValue")
    fun getValue(@RequestParam identifier: String): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            remoteStorageService.getValue(identifier)
        }
    }

    @PostMapping("/setValue")
    fun setValue(@RequestParam identifier: String, @RequestParam value: String): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            remoteStorageService.setValue(identifier, value)
        }
    }

    @DeleteMapping("/deleteValue")
    fun deleteValue(@RequestParam identifier: String): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            remoteStorageService.deleteValue(identifier)
        }
    }
}