package jsonweb.exitserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@SpringBootApplication
class JsonWebServerApplication

@ApiIgnore
@RestController
class HelloController {
    @GetMapping
    fun hello() = "hello!"
}

fun main(args: Array<String>) {
    runApplication<JsonWebServerApplication>(*args)
}
