package jsonweb.exitserver.common

import jsonweb.exitserver.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = logger()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun globalErrorHandle(ex: Exception): ResponseEntity<CommonResponse<Any>> {
        log.warn("[{}] handled: {}", ex.javaClass.simpleName, ex.message)
        return badRequest(ex.message ?: "entity not found exception")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun validationErrorHandle(ex: MethodArgumentNotValidException): ResponseEntity<CommonResponse<Any>> {
        var message = ""
        ex.bindingResult.fieldErrors.forEach {
            message += it.defaultMessage
        }
        log.warn("[{}] handled: {}", ex.javaClass.simpleName, message)
        return badRequest(message)
    }
}
