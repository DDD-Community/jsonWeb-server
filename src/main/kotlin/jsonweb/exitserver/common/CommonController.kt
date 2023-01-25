package jsonweb.exitserver.common

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@ApiIgnore
@RestController
class HelloController {
    @GetMapping
    fun hello() = "hello!"
}

@Controller
class VueClientRedirector {
    @GetMapping(value = [
         "/admin-page/**",
    ])
    fun redirect() = "forward:/index.html"
}