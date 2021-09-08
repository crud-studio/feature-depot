package studio.crud.feature.mediafiles.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.MediaFileService
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.ro.MediaFileRO
import studio.crud.sharedcommon.crud.web.annotations.CrudOperations
import studio.crud.sharedcommon.web.controller.AbstractSimplifiedErrorHandlingJpaCrudController
import studio.crud.feature.core.web.model.ResultRO
import javax.servlet.http.HttpServletResponse

@CrudOperations(create = false, update = false, delete = false)
abstract class AbstractMediaFileController<RO : MediaFileRO>:
    AbstractSimplifiedErrorHandlingJpaCrudController<MediaFile, RO>() {

    @Autowired
    private lateinit var mediaFileService: MediaFileService

    @PostMapping(value = ["/upload"])
    fun uploadMediaFile(
        @RequestParam(value = "file") file: MultipartFile,
        @RequestParam(required = false) alias: String?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false, defaultValue = "PRIVATE") aclMode: MediaFileAclMode
    ): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mediaFileService.uploadFile(
                file,
                alias,
                description,
                aclMode
            )
        }
    }

    @GetMapping("/download/{uuid}")
    fun getMediaFile(
        @PathVariable uuid: String,
        response: HttpServletResponse
    ) {
        try {
            mediaFileService.downloadFile(uuid, response)
        } catch (e: Exception) {
            response.status = 400
        }
    }

    @PostMapping("/uploadAndAssociate/{entityId}/{entityName}/{fieldName}")
    fun uploadAndAssociate(
        @RequestParam(value = "file") file: MultipartFile,
        @RequestParam(required = false) alias: String?,
        @RequestParam(required = false) description: String?,
        @PathVariable entityId: Long,
        @PathVariable entityName: String,
        @PathVariable fieldName: String
    ): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mediaFileService.uploadAndAssociateFile(file, alias, description, entityId, entityName, fieldName)
        }
    }

    @DeleteMapping("/deleteAssociatedMediaFile/{entityId}/{entityName}/{fieldName}")
    fun deleteAssociatedMediaFile(
        @PathVariable entityId: Long,
        @PathVariable entityName: String,
        @PathVariable fieldName: String
    ): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mediaFileService.deleteAssociatedMediaFile(entityId, entityName, fieldName)
        }
    }
}