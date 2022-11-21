package jsonweb.exitserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JsonWebServerApplication

fun main(args: Array<String>) {
    runApplication<JsonWebServerApplication>(*args)
}
