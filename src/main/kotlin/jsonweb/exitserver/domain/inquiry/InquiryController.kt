package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inquiry")
class InquiryController(private val inquiryService: InquiryService) {
    @GetMapping("/category")
    fun getInquiryCategories(): CommonResponse<List<String>> = success(INQUIRY_CATEGORIES)

    @PostMapping
    fun createInquiry(@RequestBody form: InquiryRequest): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.createInquiry(form))

    @GetMapping
    fun getMyInquiries(): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.getMyInquiries())

    @GetMapping("/{inquiryId}")
    fun getInquiry(@PathVariable inquiryId: Long): CommonResponse<InquiryResponse> =
        success(inquiryService.getInquiryToDto(inquiryId))

    @PutMapping("/{inquiryId}")
    fun deleteInquiry(
        @PathVariable inquiryId: Long,
        @RequestBody form: InquiryRequest
    ): CommonResponse<List<InquiryResponse>> = success(inquiryService.updateInquiry(inquiryId, form))


    @DeleteMapping("/{inquiryId}")
    fun deleteInquiry(@PathVariable inquiryId: Long): CommonResponse<Any> {
        inquiryService.deleteInquiry(inquiryId)
        return success(null)
    }
}

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/inquiry")
class AdminInquiryController(private val inquiryService: InquiryService) {

    @GetMapping("/all")
    fun getAllInquiries(): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.getAllInquiries())

    @PostMapping("/{inquiryId}/resolve")
    fun resolveInquiry(
        @PathVariable("inquiryId") id: Long,
        @RequestBody form: InquiryAnswerDto
    ): CommonResponse<InquiryResponse> =
        success(inquiryService.resolveInquiry(id, form))
}