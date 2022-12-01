package jsonweb.exitserver.util.s3

import org.springframework.web.multipart.MultipartFile

enum class S3TypeDir(private val path: String, private val suffix: String) {
    CAFE("/cafe/", "_cafe_image.png"),
    THEME("/theme/", "_theme_image.png"),
    PROFILE("/profile/", "_profile_image.png"),
    CERTIFICATION("/certification/", "_certification_image.png");

    fun getDir() = path
    fun getSuffix() = suffix
}

data class UploadImageRequest(var image: MultipartFile)
data class UploadImageResponse(var imageName: String, var imageUrl: String)