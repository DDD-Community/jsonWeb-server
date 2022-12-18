package jsonweb.exitserver.domain.review

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
    private val reviewRepositoryImpl: ReviewRepositoryImpl,
    private val reviewLikeRepository: ReviewLikeRepository,
    private val themeRepository: ThemeRepository,
    private val userService: UserService,
    private val modelMapper: ModelMapper
) {
    val emotions = Emotions()

    @Transactional
    fun createReview(themeId: Long, form: CreateReviewRequest) {
        val user = userService.getCurrentLoginUser()
        val theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }
        val review = modelMapper.map(ReviewWithTheme(form, theme, user), Review::class.java)
        reviewRepository.save(review)
    }

    @Transactional
    fun updateReview(reviewId: Long, form: UpdateReviewRequest) {
        val review = reviewRepository.findById(reviewId).orElseThrow { throw EntityNotFoundException() }
        review.editReview(form.emotionFirst, form.emotionSecond, form.content, form.star, form.difficulty)
    }

    @Transactional
    fun deleteReview(reviewId: Long) {
        reviewRepository.deleteById(reviewId)
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
        val totalReviewCount = reviewRepository.findAllByTheme(theme).count()

        var popularEmotion: String = ""
        var popularEmotionCount = 0

        for (emotion in emotions.list) {
            val tempCount = reviewRepositoryImpl.countWithEmotion(emotion)
            if (tempCount > popularEmotionCount) {
                popularEmotionCount = tempCount
                popularEmotion = emotion
            }
        }
        val percentage = popularEmotionCount / totalReviewCount * 100
        return PopularEmotionResponse(percentage, popularEmotion)
    }

    private fun makeSort(sort: String): Sort {
        return Sort.by(
            ReviewSort.valueOf(sort).getDirection(), ReviewSort.valueOf(sort).getSortBy()
        ).and(Sort.by("reviewId"))
    }
}