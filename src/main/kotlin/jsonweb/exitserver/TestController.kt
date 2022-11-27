package jsonweb.exitserver

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.logger
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    val log = logger()

    @GetMapping
    fun hello() = "hello!"

    @GetMapping("/success")
    fun successTest(): CommonResponse<Test> {
        log.info("success")
        val data = Test("test")
        return success(data)
    }

    @GetMapping("/fail")
    fun badRequestTest(): CommonResponse<Test> {
        log.warn("badRequest")
        throw RuntimeException("badRequest")
    }

}