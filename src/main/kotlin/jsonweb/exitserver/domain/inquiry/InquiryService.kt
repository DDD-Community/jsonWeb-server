package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.INQUIRY_CANCEL_ERROR
import jsonweb.exitserver.common.InquiryException
import jsonweb.exitserver.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class InquiryService(
    private val userService: UserService,
    private val inquiryRepository: InquiryRepository
) {

    private fun getInquiry(id: Long): Inquiry = userService.getCurrentLoginUser().inquiryList
        .find { it.inquiryId == id } ?: throw EntityNotFoundException()

    /**
     * 사용자
     */
    fun getUserInquires(): List<InquiryResponse> =
        userService.getCurrentLoginUser().inquiryList
            .map { InquiryResponse(it) }

    fun getInquiryToDto(id: Long) = InquiryResponse(getInquiry(id))

    @Transactional
    fun createInquiry(form: InquiryRequest): List<InquiryResponse> {
        val user = userService.getCurrentLoginUser()
        val inquiry = Inquiry(
            category = form.category,
            user = user,
            title = form.title,
            content = form.content
        )
        user.addInquiry(inquiry)
        inquiryRepository.flush()
        return user.inquiryList.map { InquiryResponse(it) }
    }

    @Transactional
    fun updateInquiry(id: Long, form: InquiryRequest): List<InquiryResponse> {
        val inquiry = getInquiry(id)
        inquiry.update(form.category, form.title, form.content)
        inquiryRepository.flush()
        return userService.getCurrentLoginUser().inquiryList.map { InquiryResponse(it) }
    }

    @Transactional
    fun deleteInquiry(id: Long) {
        val inquiry = getInquiry(id)
        val user = userService.getCurrentLoginUser()
        if (inquiry.status == InquiryStatus.WAITING)
            user.deleteInquiry(inquiry)
        else
            throw InquiryException(INQUIRY_CANCEL_ERROR)
    }

    /**
     * 관리자
     */
    fun getAllInquiries(): List<InquiryResponse> = inquiryRepository.findAll().map { InquiryResponse(it) }

    @Transactional
    fun resolveInquiry(id: Long, form: InquiryAnswerDto): InquiryResponse {
        val inquiry = getInquiry(id)
        if (inquiry.status == InquiryStatus.WAITING) {
            inquiry.resolve()
            inquiry.addAnswer(form.answer)
            inquiryRepository.flush()
            return InquiryResponse(inquiry)
        } else {
            throw InquiryException(INQUIRY_CANCEL_ERROR)
        }
    }

    /**
     * 테스트용
     */
    @Transactional
    fun createDummyInquiry(form: InquiryRequest, dummyKakaoId: Long): List<InquiryResponse> {
        val user = userService.getTestUser(dummyKakaoId)
        val inquiry = Inquiry(
            category = form.category,
            user = user,
            title = form.title,
            content = form.content
        )
        user.addInquiry(inquiry)
        inquiryRepository.flush()
        return user.inquiryList.map { InquiryResponse(it) }
    }
}