package jsonweb.exitserver.util.s3

import org.springframework.web.multipart.MultipartFile

enum class S3TypeDir(private val path: String, private val suffix: String) {
    CAFE("/cafes/", "_cafe_image.png"),
    THEME("/themes/", "_theme_image.png"),
    PROFILE("/profiles/", "_profile_image.png"),
    BOAST("/boasts/", "_boast_image.png");

    fun getDir() = path
    fun getSuffix() = suffix
}

data class UploadImageRequest(var image: MultipartFile)
data class UploadImageResponse(var imageName: String, var imageUrl: String)