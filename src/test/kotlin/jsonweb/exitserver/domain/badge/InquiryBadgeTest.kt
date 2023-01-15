package jsonweb.exitserver.domain.badge

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import jsonweb.exitserver.domain.getMockUser
import jsonweb.exitserver.domain.inquiry.InquiryCategoryEnum
import jsonweb.exitserver.domain.inquiry.InquiryRepository
import jsonweb.exitserver.domain.inquiry.InquiryRequest
import jsonweb.exitserver.domain.inquiry.InquiryService
import jsonweb.exitserver.domain.user.UserService
import jsonweb.exitserver.util.badge.BadgeEnum
import jsonweb.exitserver.util.badge.CheckBadgeAspect
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory

class InquiryBadgeTest : AnnotationSpec() {
    private val userService: UserService = mockk()
    private val inquiryRepository: InquiryRepository = mockk()

    private val inquiryService = InquiryService(
        userService, inquiryRepository
    )

    private val mockUser = getMockUser()

    private lateinit var factory: AspectJProxyFactory
    private lateinit var inquiryServiceProxy: InquiryService

    @BeforeAll
    fun init() {
        every { userService.getCurrentLoginUser() } returns mockUser
        justRun { inquiryRepository.flush() }

        factory = AspectJProxyFactory(inquiryService)
        factory.addAspect(CheckBadgeAspect(userService))
        inquiryServiceProxy = factory.getProxy()
    }

    @Test
    fun `트렌드세터 - 카페 등록 요청 5번`() {
        // given
        val form = InquiryRequest(InquiryCategoryEnum.CAFE.kor(), "", "")

        // when
        repeat(5) {
            inquiryServiceProxy.createInquiry(form)
        }

        // then
        mockUser.badgeList.any {
            it.badge == BadgeEnum.TREND_SETTER.kor()
        } shouldBe true
    }
}