package jsonweb.exitserver.s3

import org.springframework.web.multipart.MultipartFile

data class UploadImageRequest(
    var image: MultipartFile
)
