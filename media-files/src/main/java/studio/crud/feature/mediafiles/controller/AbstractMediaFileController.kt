package studio.crud.feature.mediafiles.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.MediaFileService
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.ro.MediaFileRO
import studio.crud.sharedcommon.crud.web.annotations.CrudOperations
import studio.crud.sharedcommon.web.controller.AbstractSimplifiedErrorHandlingJpaCrudController
import studio.crud.sharedcommon.web.ro.ResultRO
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
        @RequestParam(required = false) description: String?
    ): ResponseEntity<ResultRO<*>> {
        return wrapResult {
            mediaFileService.uploadFile(
                    file,
                    alias,
                    description
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
}