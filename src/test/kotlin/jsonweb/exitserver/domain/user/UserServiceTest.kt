package jsonweb.exitserver.domain.user

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jsonweb.exitserver.auth.KakaoClient
import jsonweb.exitserver.auth.KakaoUserInfo
import jsonweb.exitserver.auth.jwt.JwtProvider
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserRepository
import jsonweb.exitserver.domain.user.UserService
import jsonweb.exitserver.util.NicknameGenerator
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserServiceTest : AnnotationSpec() {

    private val userRepository = mockk<UserRepository>()
    private val kakaoClient = mockk<KakaoClient>()
    private val jwtProvider = mockk<JwtProvider>()
    private val passwordEncoder = mockk<BCryptPasswordEncoder>()
    private val nicknameGenerator = mockk<NicknameGenerator>()
    private val userService: UserService = UserService(
        userRepository,
        kakaoClient,
        jwtProvider,
        passwordEncoder,
        nicknameGenerator,
    )
    
    private val testJwt = "jwt"
    private val testUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
    

    @BeforeAll
    fun stubbing() {
        every { passwordEncoder.encode(any()) } returns "encodedPassword"
        every { kakaoClient.getKakaoUserInfo(any()) } returns KakaoUserInfo(-1, "male", "20~29")
        every { userRepository.findByKakaoId(any()) } returns testUser
        every { nicknameGenerator.getRandomNickname() } returns "랜덤 닉네임"
        every { jwtProvider.generateToken(testUser) } returns testJwt
    }

    @Test
    fun `카카오 로그인 성공 - 기존 회원 로그인`() {
        val jwtDto = userService.login("asd")
        jwtDto.accessToken shouldBe testJwt
    }

    @Test
    fun `카카오 로그인 성공 - 새로운 회원 가입 진행 후 로그인 처리`() {
        every { userRepository.findByKakaoId(any()) } returns null
        every { userRepository.save(any()) } returns testUser

        val jwtDto = userService.login("asd")

        verify(exactly = 1) { userRepository.save(any()) }
        jwtDto.accessToken shouldBe testJwt
    }
}