package jsonweb.exitserver.domain.inquiry

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserService

class InquiryServiceTest: AnnotationSpec() {
    private val userService = mockk<UserService>()
    private val inquiryRepository = mockk<InquiryRepository>()
    private val inquiryService = InquiryService(userService, inquiryRepository)

    private val testUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")

    @BeforeAll
    fun stubbing() {
        every { userService.getCurrentLoginUser() } returns testUser
    }

    @Test
    fun `카페등록문의 작성 - 성공`() {
        // given
        val form = RegisterInquiryRequest("등록카페이름", "서울시 강남구", "요청사항1")

        // when
        val result: List<InquirySimpleResponse> = inquiryService.registerInquiry(form)

        // then
        val expect = result.last()
        testUser.inquiryList.size shouldBe 1
        expect.cafeName shouldBe "등록카페이름"
        expect.status shouldBe InquiryStatus.WAITING

    }
}