package jsonweb.exitserver.domain.inquiry

import javax.validation.constraints.Size

data class RegisterInquiryRequest(
    val cafeName: String,
    val address: String,

    @field:Size(max = 500, message = "over the max length about description. (maximum 500)")
    val description: String
)

data class InquiryResponse(
    val id: Long,
    val cafeName: String,
    val address: String,
    val description: String,
    val status: String,
    val createdAt: String
) {
    constructor(inquiry: Inquiry): this(
        id = inquiry.inquiryId,
        cafeName = inquiry.cafeName,
        address = inquiry.address,
        description = inquiry.description,
        status = when(inquiry.status) {
            InquiryStatus.PROCEEDING -> "진행 중"
            InquiryStatus.DONE -> "등록 완료"
            InquiryStatus.CANCEL -> "취소 됨"
            else -> "등록 요청 중"
        },
        createdAt = inquiry.createdAt
    )
}

//data class InquirySimpleResponse(
//    val createdAt: String,
//    val cafeName: String,
//    val status: String
//) {
//    constructor(inquiry: Inquiry): this(
//        createdAt = inquiry.createdAt,
//        cafeName = inquiry.cafeName,
//        status = when(inquiry.status) {
//            InquiryStatus.PROCEEDING -> "진행 중"
//            InquiryStatus.DONE -> "등록 완료"
//            else -> "등록 요청 중"
//        }
//    )
//}