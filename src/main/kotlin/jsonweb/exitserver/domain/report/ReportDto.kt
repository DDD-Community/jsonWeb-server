package jsonweb.exitserver.domain.report

data class BoastReportRequest(
    val boastId: Long,
    val content: String
)

data class ReviewReportRequest(
    val reviewId: Long,
    val content: String
)