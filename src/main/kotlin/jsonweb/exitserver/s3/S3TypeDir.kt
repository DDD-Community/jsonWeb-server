package jsonweb.exitserver.s3

enum class S3TypeDir(
    private val path: String,
    private val suffix: String
) {
    CAFE("/cafe/", "_cafe_image.png"),
    THEME("/theme/", "_theme_image.png"),
    PROFILE("/profile/", "_profile_image.png"),
    CERTIFICATION("/certification/", "_certification_image.png")
    ;

    fun getDir() = path
    fun getSuffix() = suffix
}