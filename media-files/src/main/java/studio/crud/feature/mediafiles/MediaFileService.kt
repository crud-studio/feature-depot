package studio.crud.feature.mediafiles

import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.ro.MediaFileRO
import javax.servlet.http.HttpServletResponse

interface MediaFileService {
    fun uploadFile(file: MultipartFile, alias: String?, description: String?, aclMode: MediaFileAclMode): MediaFileRO
    fun uploadAndAssociateFile(file: MultipartFile, alias: String?, description: String?, entityId: Long, entityName: String, fieldName: String): MediaFileRO
    fun downloadFile(uuid: String, response: HttpServletResponse)
    fun deleteAssociatedMediaFile(entityId: Long, entityName: String, fieldName: String)
}