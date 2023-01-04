package jsonweb.exitserver.domain.user

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/test-login")
    fun testLogin(): CommonResponse<JwtDto> =
        success(userService.testLogin())

    @GetMapping("/login")
    fun kakaoLogin(code: String): CommonResponse<JwtDto> =
        success(userService.login(code))

    @GetMapping("/me")
    fun getCurrentLoginUser(): CommonResponse<UserInfoResponse> =
        success(userService.getCurrentLoginUserToDto())

    @PutMapping("/me")
    fun updateUser(@RequestBody form: UpdateUserInfoRequest): CommonResponse<UserInfoResponse> =
        success(userService.updateUserInfo(form))

    @PostMapping("/logout")
    fun logout(): CommonResponse<Boolean> =
        success(userService.logout())

    @DeleteMapping("/me")
    fun deleteUser(): CommonResponse<Boolean> =
        success(userService.deleteUser())
}