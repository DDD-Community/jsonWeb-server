package jsonweb.exitserver.util.s3

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/images")
class S3Controller(private val s3Service: S3Service) {
    @PostMapping("/{type}")
    fun uploadImage(
        @PathVariable("type") type: String,
        @ModelAttribute form: UploadImageRequest
    ): CommonResponse<UploadImageResponse> =
        success(s3Service.uploadImage(type, form.image))

    @DeleteMapping("/{type}/{imageName}")
    fun deleteImage(
        @PathVariable("type") type: String,
        @PathVariable("imageName") imageName: String
    ): CommonResponse<Any> {
        s3Service.deleteImage(type, imageName)
        return success()
    }

    @PostMapping("/{type}/{imageName}")
    fun updateImage(
        @PathVariable("type") type: String,
        @PathVariable("imageName") imageName: String,
        @ModelAttribute form: UploadImageRequest
    ): CommonResponse<UploadImageResponse> {
        s3Service.deleteImage(type, imageName)
        return success(s3Service.uploadImage(type, form.image))
    }
}