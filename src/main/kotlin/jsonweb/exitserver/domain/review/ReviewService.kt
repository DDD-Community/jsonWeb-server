package jsonweb.exitserver.domain.review

import jsonweb.exitserver.domain.cafe.CafeRepository
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.UserService
import org.modelmapper.ModelMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val reviewLikeRepository: ReviewLikeRepository,
    private val themeRepository: ThemeRepository,
    private val cafeRepository: CafeRepository,
    private val userService: UserService,
    private val modelMapper: ModelMapper
) {

    @Transactional
    fun createReview(themeId: Long, form: CreateReviewRequest) {
        val user = userService.getCurrentLoginUser()
        val theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }
        val review = modelMapper.map(ReviewWithTheme(form, theme, user), Review::class.java)
        reviewRepository.save(review)
        theme.addReview(review)
        theme.cafe.addReview(form.star)
    }

    @Transactional
    fun updateReview(reviewId: Long, form: UpdateReviewRequest) {
        val review = reviewRepository.findById(reviewId).orElseThrow { throw EntityNotFoundException() }
        review.theme.deleteReview(review)
        review.theme.cafe.editStar(form.star - review.star)
        review.editReview(form.emotionFirst, form.emotionSecond, form.content, form.star, form.difficulty)
        review.theme.addReview(review)
    }

    @Transactional
    fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findById(reviewId).orElseThrow { throw EntityNotFoundException() }
        review.theme.addReview(review)
        review.theme.cafe.deleteReview(review.star)
        reviewRepository.deleteById(reviewId)
    }

    fun getReview(reviewId: Long): ReviewResponse {
        val review = reviewRepository.findById(reviewId).orElseThrow { throw EntityNotFoundException() }
        return markLike(ReviewResponse(review))
    }

    fun checkLike(reviewId: Long) {
        val userId = userService.getCurrentLoginUser().userId
        val review = reviewRepository.findById(reviewId).orElseThrow { throw EntityNotFoundException() }
        if (reviewLikeRepository.existsById(UserAndReview(userId, reviewId))) {
            likeReview(userId, reviewId)
            review.plusLike()
        } else {
            unlikeReview(userId, reviewId)
            review.minusLike()
        }
    }

    @Transactional
    fun likeReview(userId: Long, reviewId: Long) {
        reviewLikeRepository.save(ReviewLike(userId, reviewId))
    }

    @Transactional
    fun unlikeReview(userId: Long, cafeId: Long) {
        reviewLikeRepository.deleteById(UserAndReview(userId, cafeId))
    }

    fun getReviewList(themeId: Long, page: Int, size: Int, sort: String): ReviewListResponse {
        val theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }
        val pageable = PageRequest.of(
            page, size, makeSort(sort)
        )
        val reviews = reviewRepository.findAllByTheme(theme, pageable)
        return markLike(
            ReviewListResponse(
                reviews.toList().map { ReviewResponse(it) },
                reviews.totalElements,
                reviews.isLast
            )
        )
    }

    fun getUserReviewList(page: Int, size: Int, sort: String): ReviewListResponse {
        val user = userService.getCurrentLoginUser()
        val pageable = PageRequest.of(
            page, size, makeSort(sort)
        )
        val reviews = reviewRepository.findAllByUser(user, pageable)
        return markLike(
            ReviewListResponse(
                reviews.toList().map { ReviewResponse(it) },
                reviews.totalElements,
                reviews.isLast
            )
        )
    }

    private fun markLike(reviewResponse: ReviewResponse): ReviewResponse {
        val userId = userService.getCurrentLoginUser().userId
        val likes = reviewLikeRepository.findAllByUserId(userId).map { it.reviewId }
        if (reviewResponse.reviewId in likes) reviewResponse.isLiked = true
        return reviewResponse
    }

    private fun markLike(reviewListResponse: ReviewListResponse): ReviewListResponse {
        val userId = userService.getCurrentLoginUser().userId
        val likes = reviewLikeRepository.findAllByUserId(userId).map { it.reviewId }
        for (reviewResponse in reviewListResponse.reviewList) {
            if (reviewResponse.reviewId in likes) reviewResponse.isLiked = true
        }
        return reviewListResponse
    }

    fun getPopularEmotion(themeId: Long): PopularEmotionResponse {
        val theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }
        val reviews = reviewRepository.findAllByTheme(theme)
        val totalReviewCount = reviews.count()

        if (totalReviewCount == 0) return PopularEmotionResponse(0, "No Review")

        var emotionCount = mutableMapOf<String, Int>()
        val emotionEmoji = mutableMapOf<String, String>()
        Emotions.values().forEach { emotionCount[it.getEmotion()] = 0 }
        Emotions.values().forEach { emotionEmoji[it.getEmotion()] = it.getEmoji()}

        for (review in reviews) {
            incrementCount(review.emotionFirst, emotionCount)
            incrementCount(review.emotionSecond, emotionCount)
        }

        val maxEmotion = emotionCount.maxWith { o1, o2 -> o1.value.compareTo(o2.value) }

        val percentage = 100 * maxEmotion.value / totalReviewCount
        return PopularEmotionResponse(percentage, maxEmotion.key + emotionEmoji[maxEmotion.key])
    }

    private fun incrementCount(emotion: String, emotionCount: MutableMap<String, Int>) {
        if (!emotion.isNullOrEmpty()) {
            val curCount = emotionCount[emotion]
            emotionCount[emotion] = curCount!! + 1
        }
    }

    private fun makeSort(sort: String): Sort {
        return Sort.by(
            ReviewSort.valueOf(sort).getDirection(), ReviewSort.valueOf(sort).getSortBy()
        ).and(Sort.by("reviewId"))
    }
}