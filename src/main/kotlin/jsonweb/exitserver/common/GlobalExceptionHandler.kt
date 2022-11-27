package jsonweb.exitserver.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = logger()

    @ExceptionHandler(Exception::class)
    fun globalErrorHandle(ex: Exception): ResponseEntity<CommonResponse<Any>> {
        log.warn("[{}] handled: {}", ex.javaClass.simpleName, ex.message)
        return badRequest(ex.message!!)
    }
}
