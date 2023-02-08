package jsonweb.exitserver.common

import jsonweb.exitserver.util.badge.BadgeResponse
import jsonweb.exitserver.util.badge.BadgeEnum
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
    @GetMapping(
        value = [
            "/admin",
            "/admin-page/**",
        ]
    )
    fun redirect() = "forward:/index.html"
}

@RestController
class BadgeController {
    @GetMapping("/badges")
    fun getAllBadges(): CommonResponse<List<BadgeResponse>> = success(
        BadgeEnum.values().map { BadgeResponse(it.kor(), it.requirement(), it.order()) }
    )
}