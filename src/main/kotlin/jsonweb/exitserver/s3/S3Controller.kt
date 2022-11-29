package jsonweb.exitserver.s3

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/image")
class S3Controller(
    private val s3Service: S3Service
) {
    @PostMapping("/{type}")
    fun uploadImage(
        @PathVariable("type") type: String, @ModelAttribute uploadImageRequest: UploadImageRequest
    ): CommonResponse<UploadImageResponse> = success(s3Service.uploadImage(type, uploadImageRequest.image))

    @DeleteMapping("/{type}/{imageName}")
    fun deleteImage(
        @PathVariable("type") type: String, @PathVariable("imageName") imageName: String
    ): CommonResponse<Any> {
        s3Service.deleteImage(type, imageName)
        return success()
    }

}