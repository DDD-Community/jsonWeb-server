package jsonweb.exitserver.domain.review

data class ReviewResponse(
    val reviewId: Long,
    val isLiked: Boolean,
    val likeCount: Int,
    val writerNickname: String,
    val createdAt: String,
    val modifiedAt: String,
    val star: Double,
    val difficulty: Double,
    val content: String
) {
    constructor(review: Review): this(
        reviewId = review.reviewId,
        isLiked = false, // TODO: 관련 작업 후 수정
        likeCount = review.likeCount,
        writerNickname = "", // TODO: 관련 작업 후 수정
        createdAt = review.createdAt,
        modifiedAt = review.modifiedAt,
        star = review.star,
        difficulty = review.difficulty,
        content = review.content
    )
}