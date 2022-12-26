package jsonweb.exitserver.domain.user

import jsonweb.exitserver.auth.KakaoClient
import jsonweb.exitserver.auth.jwt.JwtProvider
import jsonweb.exitserver.auth.security.getCurrentLoginUserId
import jsonweb.exitserver.common.TEST_ADMIN_KAKAO_ID
import jsonweb.exitserver.common.USER_NOT_FOUND
import jsonweb.exitserver.common.UserException
import jsonweb.exitserver.util.NicknameGenerator
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val kakaoClient: KakaoClient,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val nicknameGenerator: NicknameGenerator
) {

    fun getCurrentLoginUser(): User = userRepository.findById(getCurrentLoginUserId())
        .orElseThrow { UserException(USER_NOT_FOUND) }

    fun getCurrentLoginUserToDto() = UserInfoResponse(getCurrentLoginUser())

    @Transactional
    fun login(authorizedCode: String): JwtDto {
        val kakaoUserInfo = kakaoClient.getKakaoUserInfo(authorizedCode)
        var user = userRepository.findByKakaoId(kakaoUserInfo.kakaoId)

        /* username, password 는 아래 UsernamePasswordAuthenticationToken 의 형식을 맞춰주기 위해
        이름을 변경한 것 */
        val username = kakaoUserInfo.kakaoId

        // password 는 서비스에서 사용X, Security 설정을 위해 넣어준 값
        val password = username.toString()

        val age = kakaoUserInfo.ageRange
        val gender = kakaoUserInfo.gender

        if (user == null) {
            // 회원가입 진행
            val newUser = User(
                kakaoId = username,
                nickname = nicknameGenerator.getRandomNickname(),
                ageRange = age,
                gender = gender,
                password = passwordEncoder.encode(password)
            )
            user = userRepository.save(newUser)
        }

        // 로그인 처리
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(username, password)

        // JWT 발급
        return JwtDto(jwtProvider.generateToken(user))
    }

    @Transactional
    fun updateUserInfo(form: UpdateUserInfoRequest): UserInfoResponse {
        val user = getCurrentLoginUser()
        user.updateUserInfo(
            newNickname = form.newNickname,
            newProfileImageUrl = form.newProfileImageUrl
        )
        return UserInfoResponse(user)
    }

    fun logout(): Boolean = kakaoClient.logout(getCurrentLoginUser().kakaoId)

    @Transactional
    fun deleteUser(): Boolean {
        val user = getCurrentLoginUser()
        userRepository.delete(user)
        return kakaoClient.unlink(user.kakaoId)
    }

    /**
     * 테스트용
     */
    @Transactional
    fun testLogin(): JwtDto {
        val username = TEST_ADMIN_KAKAO_ID
        val password = "1234"
        var testUser = userRepository.findByKakaoId(TEST_ADMIN_KAKAO_ID)
        if (testUser == null) {
            val newUser = User(
                kakaoId = TEST_ADMIN_KAKAO_ID,
                nickname = nicknameGenerator.getRandomNickname(),
                ageRange = "20~29",
                gender = "male",
                password = passwordEncoder.encode(password)
            )
            newUser.setAdmin()
            testUser = userRepository.save(newUser)
        }
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(username, password)
        return JwtDto(jwtProvider.generateToken(testUser))
    }

    fun getTestUser(kakaoId: Long) = userRepository.findByKakaoId(kakaoId)!!

}