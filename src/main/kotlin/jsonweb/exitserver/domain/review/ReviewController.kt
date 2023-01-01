package jsonweb.exitserver.domain.review

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @PostMapping("/themes/{themeId}/reviews")
    fun createReview(
        @PathVariable("themeId") themeId: Long,
        @RequestBody form: CreateReviewRequest
    ): CommonResponse<Any> {
        reviewService.createReview(themeId, form)
        return success()
    }

    @PutMapping("/reviews/{reviewId}")
    fun updateReview(
        @PathVariable("reviewId") reviewId: Long,
        @RequestBody form: UpdateReviewRequest
    ): CommonResponse<Any> {
        reviewService.updateReview(reviewId, form)
        return success()
    }

    @DeleteMapping("/reviews/{reviewId}")
    fun deleteReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<Any> {
        reviewService.deleteReview(reviewId)
        return success()
    }

    @GetMapping("/reviews/{reviewId}")
    fun getReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<ReviewResponse> =
        success(reviewService.getReview(reviewId))


    @PutMapping("/reviews/{reviewId}/like")
    fun likeReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<Any> {
        reviewService.checkLike(reviewId)
        return success()
    }

    @GetMapping("/themes/{themeId}/reviews")
    fun getReviewList(
        @PathVariable("themeId") themeId: Long,
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<ReviewListResponse> = success(reviewService.getReviewList(themeId, page, size, sort))

    @GetMapping("/users/me/reviews")
    fun getUserReviewList(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<ReviewListResponse> = success(reviewService.getUserReviewList(page, size, sort))

    @GetMapping("/themes/{themeId}/reviews/popular-emotion")
    fun getPopularEmotion(@PathVariable("themeId") themeId: Long): CommonResponse<PopularEmotionResponse> =
        success(reviewService.getPopularEmotion(themeId))

}