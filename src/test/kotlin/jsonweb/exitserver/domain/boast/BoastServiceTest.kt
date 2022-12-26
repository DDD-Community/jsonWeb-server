package jsonweb.exitserver.domain.boast

import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserService

class BoastServiceTest : AnnotationSpec() {
    private val userService: UserService = mockk()
    private val themeRepository: ThemeRepository = mockk()
    private val boastRepository: BoastRepository = mockk()
    private val boastLikeRepository: BoastLikeRepository = mockk()

    private val boastService: BoastService = spyk(
        BoastService(userService, themeRepository, boastRepository, boastLikeRepository),
        recordPrivateCalls = true
    )

    private val testUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")

    @BeforeAll
    fun stub() {
        every { userService.getCurrentLoginUser() } returns testUser
    }

    @Test
    fun `인증 전체 조회`() {
        // given

        // when
        val boastList = boastService.getAllBoasts("DATE", 0, 16)

        // then
    }
}