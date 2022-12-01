package jsonweb.exitserver.util.s3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class S3Service(
    private val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    @Value("\${cloud.aws.s3.dir}") private val dir: String
) {
    val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
    fun uploadImage(type: String, file: MultipartFile): UploadImageResponse {
        val imageName = generateImageName(type)
        val path = makePath(type, imageName)
        val objMeta = ObjectMetadata()
        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()
        val byteArrayInputStream = ByteArrayInputStream(bytes)

        amazonS3Client.putObject(
            PutObjectRequest(
                bucket, path, byteArrayInputStream, objMeta
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        )

        return UploadImageResponse(imageName, amazonS3Client.getUrl(bucket, path).toString())
    }

    fun deleteImage(type: String, imageName: String) {
        val path = makePath(type, imageName)
        amazonS3Client.deleteObject(DeleteObjectRequest(bucket, path))
    }

    private fun makePath(type: String, imageName: String): String {
        var path = dir
        path += S3TypeDir.valueOf(type.uppercase()).getDir()
        path += imageName
        return path
    }

    private fun generateImageName(type: String): String {
        var generatedImageName = "";
        generatedImageName += LocalDateTime.now().format(dateFormat)
        generatedImageName += ("_" + generateRandomNumber())
        generatedImageName += S3TypeDir.valueOf(type.uppercase()).getSuffix()
        return generatedImageName
    }

    private fun generateRandomNumber(): String {
        return DecimalFormat("000").format((0..999).random())
    }
}