package jsonweb.exitserver.domain.badge

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jsonweb.exitserver.domain.getMockReview
import jsonweb.exitserver.domain.getMockUser
import jsonweb.exitserver.domain.review.CreateReviewRequest
import jsonweb.exitserver.domain.review.Review
import jsonweb.exitserver.domain.review.ReviewService
import jsonweb.exitserver.domain.theme.*
import jsonweb.exitserver.domain.user.UserService
import jsonweb.exitserver.util.badge.BadgeEnum
import jsonweb.exitserver.util.badge.CheckBadgeAspect
import org.modelmapper.ModelMapper
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import java.util.*

class ReviewBadgeTest : AnnotationSpec() {
    private val userService: UserService = mockk()
    private val themeRepository: ThemeRepository = mockk()
    private val modelMapper: ModelMapper = mockk()

    private val reviewService = ReviewService(
        mockk(),
        mockk(),
        themeRepository,
        userService,
        modelMapper
    )

    private val mockUser = getMockUser()
    private val mockReview = getMockReview()
    private val mockTheme = Theme()

    private lateinit var factory: AspectJProxyFactory
    private lateinit var reviewServiceProxy: ReviewService

    @BeforeAll
    fun init() {
        every { modelMapper.map(any(), any<Class<*>>()) } returns mockReview
        every { userService.getCurrentLoginUser() } returns mockUser
        every { themeRepository.findById(any()) } returns Optional.of(mockTheme)

        factory = AspectJProxyFactory(reviewService)
        factory.addAspect(CheckBadgeAspect(userService))
        reviewServiceProxy = factory.getProxy()
    }

    @Test
    fun `엑시터 - 첫 리뷰 작성`() {
        // given
        val form = CreateReviewRequest("", "", 1.0, 1.0, "")

        // when
        reviewServiceProxy.createReview(1L, form)

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.EIXTER.kor()
        } shouldBe true
    }

    @Test
    fun `공포매니아 - 공포 장르 리뷰 3개 작성`() {
        // given
        every { modelMapper.map(any(), any<Class<*>>()) } returns getMockReview(GenreEnum.HORROR)
        val form = CreateReviewRequest("", "", 1.0, 1.0, "")

        // when
        repeat(3) {
            reviewServiceProxy.createReview(1L, form)
        }

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.HORROR_MANIA.kor()
        } shouldBe true
    }

    @Test
    fun `미스터리러버 - 미스터리 장르 리뷰 3개 작성`() {
        // given
        every { modelMapper.map(any(), any<Class<*>>()) } returns getMockReview(GenreEnum.MYSTERY)
        val form = CreateReviewRequest("", "", 1.0, 1.0, "")

        // when
        repeat(3) {
            reviewServiceProxy.createReview(1L, form)
        }

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.MYSTERY_LOVER.kor()
        } shouldBe true
    }

    @Test
    fun `로맨스홀릭 - 로맨스 장르 리뷰 3개 작성`() {
        // given
        every { modelMapper.map(any(), any<Class<*>>()) } returns getMockReview(GenreEnum.ROMANCE)
        val form = CreateReviewRequest("", "", 1.0, 1.0, "")

        // when
        repeat(3) {
            reviewServiceProxy.createReview(1L, form)
        }

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.ROMANCE_HOLIC.kor()
        } shouldBe true
    }

    // TODO : 엑시트보안관
}