package jsonweb.exitserver.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = logger()

    @ExceptionHandler(Exception::class)
    fun globalErrorHandle(ex: Exception): ResponseEntity<Any> {
        log.warn("[{}] handled: {}", ex.javaClass.simpleName, ex.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse(ex.message!!, null))
    }
}
