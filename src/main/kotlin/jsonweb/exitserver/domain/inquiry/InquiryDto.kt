package jsonweb.exitserver.domain.inquiry

import javax.validation.constraints.Size

data class InquiryRequest(
    val category: String,
    val title: String,

    @field:Size(max = 500, message = "over the max length about content. (maximum 500)")
    val content: String
)

data class InquiryResponse(
    val id: Long,
    val category: String,
    val title: String,
    val content: String,
    val status: String,
    val createdAt: String,
    val answer: String
) {
    constructor(inquiry: Inquiry): this(
        id = inquiry.inquiryId,
        category = inquiry.category,
        title = inquiry.title,
        content = inquiry.content,
        status = when(inquiry.status) {
            InquiryStatus.RESOLVED -> "해결"
            else -> "미해결"
        },
        createdAt = inquiry.createdAt,
        answer = inquiry.answer
    )
}

data class InquiryAnswerDto(
    val answer: String
)