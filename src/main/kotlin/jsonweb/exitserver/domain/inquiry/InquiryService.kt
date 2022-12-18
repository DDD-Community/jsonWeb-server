package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.INQUIRY_CANCEL_ERROR
import jsonweb.exitserver.common.InquiryException
import jsonweb.exitserver.domain.user.QUser.user
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
    fun getMyInquiries(): List<InquiryResponse> =
        userService.getCurrentLoginUser().inquiryList
            .map { InquiryResponse(it) }

    fun getInquiryToDto(id: Long) = InquiryResponse(getInquiry(id))

    @Transactional
    fun registerInquiry(form: RegisterInquiryRequest): List<InquiryResponse> {
        val user = userService.getCurrentLoginUser()
        val inquiry = Inquiry(
            user = user,
            cafeName = form.cafeName,
            address = form.address,
            description = form.description
        )
        user.addInquiry(inquiry)
        inquiryRepository.flush()
        return user.inquiryList.map { InquiryResponse(it) }
    }

    @Transactional
    fun cancelInquiry(id: Long): List<InquiryResponse> {
        val inquiry = getInquiry(id)
        if (inquiry.status == InquiryStatus.WAITING) inquiry.cancel()
        else throw InquiryException(INQUIRY_CANCEL_ERROR)
        inquiryRepository.flush()
        return userService.getCurrentLoginUser().inquiryList.map { InquiryResponse(it) }
    }

    /**
     * 관리자
     */
    fun getAllInquiries(): List<InquiryResponse> = inquiryRepository.findAll().map { InquiryResponse(it) }

    @Transactional
    fun updateStatus(id: Long, type: String): InquiryResponse {
        val inquiry = getInquiry(id)
        when(type) {
            "proceeding" -> inquiry.proceeding()
            "done" -> inquiry.done()
        }
        inquiryRepository.flush()
        return InquiryResponse(inquiry)
    }
}