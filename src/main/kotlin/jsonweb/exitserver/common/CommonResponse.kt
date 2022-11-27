package jsonweb.exitserver.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class CommonResponse<T>(
    var message: String = "ok",
    var data: T? = null,
)

fun <T> success(data: T): CommonResponse<T> = CommonResponse(data = data)
fun badRequest(message: String): ResponseEntity<CommonResponse<Any>> = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(CommonResponse(message = message))

fun unauthorized(): ResponseEntity<CommonResponse<Any>> = ResponseEntity
    .status(HttpStatus.UNAUTHORIZED)
    .body(CommonResponse(message = "access denied."))


