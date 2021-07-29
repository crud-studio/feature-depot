package studio.crud.feature.mediafiles

import org.springframework.web.multipart.MultipartFile
import studio.crud.feature.mediafiles.ro.MediaFileRO
import javax.servlet.http.HttpServletResponse

interface MediaFileService {
    fun uploadFile(multipartFile: MultipartFile, alias: String?, description: String?): MediaFileRO
    fun downloadFile(uuid: String, response: HttpServletResponse)
}