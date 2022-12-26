package jsonweb.exitserver.domain.review

import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Sort

enum class ReviewSort(private val sort: String, private val direction: Sort.Direction) {
    DEFAULT("cafeId", Sort.Direction.ASC),
    LIKE("likeCount", Sort.Direction.DESC),
    DATE("modifiedAt", Sort.Direction.DESC);

    fun getSortBy() = sort
    fun getDirection() = direction
}

data class UpdateReviewRequest(
    val emotionFirst: String,
    val emotionSecond: String,
    val star: Double,
    val difficulty: Double,
    val content: String
)

data class CreateReviewRequest(
    val emotionFirst: String,
    val emotionSecond: String,
    val star: Double,
    val difficulty: Double,
    val content: String
)

data class ReviewWithTheme(
    val emotionFirst: String,
    val emotionSecond: String,
    val star: Double,
    val difficulty: Double,
    val content: String,
    val theme: Theme,
    val user: User
) {
    constructor(form: CreateReviewRequest, theme: Theme, user: User) : this(
        emotionFirst = form.emotionFirst,
        emotionSecond = form.emotionSecond,
        star = form.star,
        difficulty = form.difficulty,
        content = form.content,
        theme = theme,
        user = user
    )
}

data class ReviewResponse(
    val reviewId: Long,
    var isLiked: Boolean,
    val likeCount: Int,
    val writerNickname: String,
    val modifiedAt: String,
    val star: Double,
    val difficulty: Double,
    val emotionFirst: String,
    val emotionSecond: String,
    val content: String,
    val themeName: String,
    val themeGenre: List<String>
) {
    constructor(review: Review) : this(
        reviewId = review.id,
        isLiked = false,
        likeCount = review.likeCount,
        writerNickname = review.user.nickname,
        modifiedAt = review.modifiedAt,
        star = review.star,
        difficulty = review.difficulty,
        emotionFirst = review.emotionFirst,
        emotionSecond = review.emotionSecond,
        content = review.content,
        themeName = review.theme.name,
        themeGenre = review.theme.themeGenreList.map { it.genre.genreName }
    )
}

data class ReviewListResponse(
    val reviewList: List<ReviewResponse>,
    val totalNumber: Long,
    val isLast: Boolean
)

data class PopularEmotionResponse(
    val percentage: Int,
    val emotion: String
)