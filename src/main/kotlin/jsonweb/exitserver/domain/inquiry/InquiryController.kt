package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class InquiryController(private val inquiryService: InquiryService) {
    @GetMapping("/inquires/categories")
    fun getInquiryCategories(): CommonResponse<List<String>> = success(INQUIRY_CATEGORIES)

    @GetMapping("/users/me/inquires")
    fun getUserInquires(): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.getUserInquires())

    @GetMapping("/inquires/{inquiryId}")
    fun getInquiry(@PathVariable inquiryId: Long): CommonResponse<InquiryResponse> =
        success(inquiryService.getInquiryToDto(inquiryId))

    @PostMapping("/inquires")
    fun createInquiry(@RequestBody form: InquiryRequest): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.createInquiry(form))

    @PutMapping("/inquires/{inquiryId}")
    fun updateInquiry(
        @PathVariable inquiryId: Long,
        @RequestBody form: InquiryRequest
    ): CommonResponse<List<InquiryResponse>> = success(inquiryService.updateInquiry(inquiryId, form))

    @DeleteMapping("/inquires/{inquiryId}")
    fun deleteInquiry(@PathVariable inquiryId: Long): CommonResponse<Any> {
        inquiryService.deleteInquiry(inquiryId)
        return success(null)
    }
}

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/inquires")
class AdminInquiryController(private val inquiryService: InquiryService) {

    @GetMapping
    fun getAllInquiryList(): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.getAllInquiries())

    @PostMapping("/{inquiryId}/resolve")
    fun resolveInquiry(
        @PathVariable("inquiryId") id: Long,
        @RequestBody form: InquiryAnswerDto
    ): CommonResponse<InquiryResponse> =
        success(inquiryService.resolveInquiry(id, form))
}