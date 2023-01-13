package jsonweb.exitserver.domain.badge

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import jsonweb.exitserver.domain.boast.*
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserService
import jsonweb.exitserver.util.badge.BadgeEnum
import jsonweb.exitserver.util.badge.CheckBadgeAspect
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import java.util.*

class BoastBadgeTest : AnnotationSpec() {
    private val userService: UserService = mockk()
    private val themeRepository: ThemeRepository = mockk()
    private val boastRepository: BoastRepository = mockk()
    private val boastLikeRepository: BoastLikeRepository = mockk()
    private val boastReportRepository: BoastReportRepository = mockk()

    private val boastService: BoastService = spyk(
        BoastService(userService, themeRepository, boastRepository, boastLikeRepository, boastReportRepository),
        recordPrivateCalls = true
    )

    private val mockUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
    private val mockTheme = Theme()

    private lateinit var factory: AspectJProxyFactory
    private lateinit var boastServiceProxy: BoastService

    @BeforeAll
    fun stub() {
        every { userService.getCurrentLoginUser() } returns mockUser
        every { themeRepository.findById(any()) } returns Optional.of(mockTheme)
        every { boastRepository.save(any()) } returns Boast(user = mockUser, theme = mockTheme, imageUrl = "")

        factory = AspectJProxyFactory(boastService)
        factory.addAspect(CheckBadgeAspect(userService))
        boastServiceProxy = factory.getProxy()
    }

    @AfterEach
    fun clear() {
        mockUser.clearMyBoast()
        mockUser.clearBadge()
        print(mockUser.badgeList)
    }

    @Test
    fun `탈출중독 - 인증 10개 작성`() {
        // given
        val form = BoastRequest(1L, "https://image.com/1", listOf("내용"))

        // when
        repeat(10) {
            boastServiceProxy.createBoast(form)
        }

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.ADDICTED_ESCAPE.kor()
        } shouldBe true
    }

    @Test
    fun `천상계 - 난이도 5이상 테마 인증 최초 작성`() {
        // given
        val hardTheme = Theme(difficulty = 5.0)
        every { themeRepository.findById(any()) } returns Optional.of(hardTheme)
        every { boastRepository.save(any()) } returns Boast(user = mockUser, theme = hardTheme, imageUrl = "")
        val form = BoastRequest(1L, "https://image.com/1", listOf("내용"))

        // when
        boastServiceProxy.createBoast(form)

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.HIGHEST_REALM.kor()
        } shouldBe true
    }
}