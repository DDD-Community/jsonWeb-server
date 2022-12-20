package jsonweb.exitserver.domain.inquiry

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import jsonweb.exitserver.common.InquiryException
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserService

class InquiryServiceTest : AnnotationSpec() {
    private val userService = mockk<UserService>()
    private val inquiryRepository = mockk<InquiryRepository>()

    // private 메소드 테스트 할라면 spyk로 모킹 해야 함
    private val inquiryService: InquiryService = spyk(
        InquiryService(userService, inquiryRepository),
        recordPrivateCalls = true
    )

    private val testUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")

    @BeforeAll
    fun stub() {
        every { userService.getCurrentLoginUser() } returns testUser
        justRun { inquiryRepository.flush() }
    }

    @AfterEach
    fun clear() = testUser.inquiryList.clear()


    @Test
    fun `카페등록문의 작성 성공`() {
        // given
        val form = RegisterInquiryRequest("등록카페이름", "서울시 강남구", "요청사항1")

        // when
        val result: List<InquiryResponse> = inquiryService.registerInquiry(form)

        // then
        val expect: InquiryResponse = result.last()
        testUser.inquiryList.size shouldBe 1
        expect.cafeName shouldBe "등록카페이름"
        expect.status shouldBe "등록 요청 중"
    }

    @Test
    fun `문의사항 취소 성공`() {
        // given
        val inquiry = Inquiry(user = testUser, cafeName = "cafeName", address = "address", description = "desc")
        testUser.addInquiry(inquiry)
        every { inquiryService["getInquiry"](0L) } returns inquiry

        // when
        val result = inquiryService.cancelInquiry(0L)

        // then
        result[0].status shouldBe "취소 됨"
    }

    @Test
    fun `문의사항 취소 실패`() {
        // given
        val inquiry = Inquiry(user = testUser, cafeName = "cafeName", address = "address", description = "desc")
        every { inquiryService["getInquiry"](0L) } returns inquiry
        inquiry.proceeding()
        testUser.addInquiry(inquiry)

        // when - then
        shouldThrowExactly<InquiryException> { inquiryService.cancelInquiry(0L) }
    }

    @Test
    fun `문의사항 상태 변경 성공 - 진행 중`() {
        // given
        val inquiry = Inquiry(user = testUser, cafeName = "cafeName", address = "address", description = "desc")
        every { inquiryService["getInquiry"](0L) } returns inquiry
        testUser.addInquiry(inquiry)

        // when
        val result: InquiryResponse = inquiryService.updateStatus(0L, InquiryStatus.PROCEEDING.type())

        // then
        result.status shouldBe "진행 중"
    }

    @Test
    fun `문의사항 상태 변경 성공 - 등록 완료`() {
        // given
        val inquiry = Inquiry(user = testUser, cafeName = "cafeName", address = "address", description = "desc")
        every { inquiryService["getInquiry"](0L) } returns inquiry
        testUser.addInquiry(inquiry)

        // when
        val result: InquiryResponse = inquiryService.updateStatus(0L, InquiryStatus.DONE.type())

        // then
        result.status shouldBe "등록 완료"
    }
}