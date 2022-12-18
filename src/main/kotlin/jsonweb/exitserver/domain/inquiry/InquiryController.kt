package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inquiry")
class InquiryController(private val inquiryService: InquiryService) {
    @PostMapping
    fun registerInquiry(@RequestBody form: RegisterInquiryRequest): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.registerInquiry(form))

    @GetMapping
    fun getMyInquiries(): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.getMyInquiries())

    @GetMapping("/{inquiryId}")
    fun getInquiry(@PathVariable inquiryId: Long): CommonResponse<InquiryResponse> =
        success(inquiryService.getInquiryToDto(inquiryId))

    @PostMapping("/{inquiryId}/cancel")
    fun cancelInquiry(@PathVariable inquiryId: Long): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.cancelInquiry(inquiryId))


    /**
     * 관리자 용
     */
    @GetMapping("/all")
    fun getAllInquiries(): CommonResponse<List<InquiryResponse>> =
        success(inquiryService.getAllInquiries())

    @PutMapping("/{inquiryId}")
    fun updateStatus(
        @PathVariable("inquiryId") id: Long,
        @RequestParam("type") type: String
    ): CommonResponse<InquiryResponse> =
        success(inquiryService.updateStatus(id, type))
}