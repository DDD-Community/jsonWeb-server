package jsonweb.exitserver.swagger

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import springfox.documentation.annotations.ApiIgnore

@Controller
@ApiIgnore
class SwaggerRedirector {
    @GetMapping("/api")
    fun redirect(): String {
        return "redirect:/swagger-ui/#"
    }
}