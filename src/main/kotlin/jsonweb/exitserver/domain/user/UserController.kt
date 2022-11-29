package jsonweb.exitserver.domain.user

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/oauth/kakao")
    fun kakaoLogin(code: String): CommonResponse<JwtDto> =
        success(userService.login(code))

    @GetMapping("/user/me")
    fun getCurrentLoginUser(): CommonResponse<UserInfoResponse> =
        success(userService.getCurrentLoginUserToDto())

    @PutMapping("/user")
    fun updateUserInfo(@RequestBody form: UpdateUserInfoRequest) {

    }

}