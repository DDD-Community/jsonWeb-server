package jsonweb.exitserver.domain.inquiry

import io.kotest.assertions.assertSoftly
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
    private val userService: UserService = mockk()
    private val inquiryRepository: InquiryRepository = mockk()

    // private 메소드 테스트 할라면 spyk로 모킹 해야 함
    private val inquiryService: InquiryService = spyk(
        InquiryService(userService, inquiryRepository),
        recordPrivateCalls = true
    )

    private val testUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
    private val testInquiry = Inquiry(user = testUser, category = "카페 등록 요청", title = "cafeName", content = "desc")

    @BeforeAll
    fun stub() {
        every { userService.getCurrentLoginUser() } returns testUser
        justRun { inquiryRepository.flush() }
        every { inquiryService["getInquiry"](0L) } returns testInquiry
    }

    @AfterEach
    fun clear() = testUser.clearInquiry()


    @Test
    fun `문의 작성 성공`() {
        // given
        val form = InquiryRequest("카페 등록 요청", "제목", "내용")

        // when
        val result: List<InquiryResponse> = inquiryService.createInquiry(form)

        // then
        val expect: InquiryResponse = result.last()
        testUser.inquiryList.size shouldBe 1
        expect.title shouldBe "제목"
        expect.content shouldBe "내용"
    }

    @Test
    fun `문의 수정 성공`() {
        // given
        val inquiry = Inquiry(user = testUser, category = "카페 등록 요청", title = "제목", content = "내용")
        testUser.addInquiry(inquiry)
        val form = InquiryRequest("서비스 관련", "제목2", "내용2")
        every { inquiryService["getInquiry"](0L) } returns inquiry

        // when
        val result: List<InquiryResponse> = inquiryService.updateInquiry(0L, form)

        // then
        testUser.inquiryList.size shouldBe 1
        assertSoftly(result.last()) {
            category shouldBe "서비스 관련"
            title shouldBe "제목2"
            content shouldBe "내용2"
        }
    }

    @Test
    fun `문의 삭제 성공`() {
        // given
        testUser.addInquiry(testInquiry)

        // when
        inquiryService.deleteInquiry(0L)

        // then
        testUser.inquiryList.size shouldBe 0
    }

    @Test
    fun `문의 삭제 실패`() {
        // given
        val inquiry = Inquiry(user = testUser, category = "카페 등록 요청", title = "제목", content = "내용")
        val answer = InquiryAnswerDto("")
        every { inquiryService["getInquiry"](0L) } returns inquiry
        inquiry.resolve()
        testUser.addInquiry(inquiry)

        // when - then
        shouldThrowExactly<InquiryException> { inquiryService.resolveInquiry(0L, answer) }
    }

    @Test
    fun `문의 해결`() {
        // given
        val inquiry = Inquiry(user = testUser, category = "카페 등록 요청", title = "제목", content = "내용")
        every { inquiryService["getInquiry"](0L) } returns inquiry
        val answer = InquiryAnswerDto("해결 답변")
        testUser.addInquiry(inquiry)

        // when
        val result: InquiryResponse = inquiryService.resolveInquiry(0L, answer)

        // then
        result.status shouldBe "해결"
        result.answer shouldBe "해결 답변"
    }

}