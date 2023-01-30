package jsonweb.exitserver.domain.user

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.logger
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    val log = logger()

    @PostMapping("/test-login")
    fun testLogin(): CommonResponse<JwtDto> = success(userService.testLogin())

    @GetMapping("/login")
    fun kakaoLogin(code: String): CommonResponse<JwtDto> {
        log.info(code)
        return success(userService.login(code))
    }

    @GetMapping("/me")
    fun getCurrentLoginUser(): CommonResponse<UserInfoResponse> =
        success(userService.getCurrentLoginUserToDto())

    @PutMapping("/me")
    fun updateUser(
        @Valid @RequestBody form: UpdateUserInfoRequest
    ): CommonResponse<UserInfoResponse> = success(userService.updateUserInfo(form))

    @PostMapping("/logout")
    fun logout(): CommonResponse<Boolean> = success(userService.logout())

    @DeleteMapping("/me")
    fun deleteUser(): CommonResponse<Boolean> = success(userService.deleteUser())

    @GetMapping
    fun checkNickname(
        @RequestParam ("nickname") nickname: String
    ): CommonResponse<CheckNicknameResponse> = success(userService.checkNickname(nickname))

    @GetMapping("/me/exp-log")
    fun getExpLogList(): CommonResponse<List<ExpLogResponse>> =
        success(userService.getExpLogList())
}