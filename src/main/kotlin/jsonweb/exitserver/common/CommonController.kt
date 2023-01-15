package jsonweb.exitserver.common

import jsonweb.exitserver.domain.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@ApiIgnore
@RestController
class HelloController {
    @GetMapping
    fun hello() = "hello!"
}

@RestController
class BadgeController(private val userService: UserService) {
//    @PostMapping("/badges/{badgeId}")
//    fun addBadge(@PathVariable badgeId: Long) {
//        userService.getCurrentLoginUser().addBadge(Badge.EIXTER)
//    }
}