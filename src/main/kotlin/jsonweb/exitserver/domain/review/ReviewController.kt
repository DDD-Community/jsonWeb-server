package jsonweb.exitserver.domain.review

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
    @PostMapping("/theme/{themeId}/review")
    fun createReview(
        @PathVariable("themeId") themeId: Long,
        @RequestBody form: CreateReviewRequest
    ): CommonResponse<Any> {
        reviewService.createReview(themeId, form)
        return success()
    }

    @PutMapping("/review/{reviewId}")
    fun updateReview(
        @PathVariable("reviewId") reviewId: Long,
        @RequestBody form: UpdateReviewRequest
    ): CommonResponse<Any> {
        reviewService.updateReview(reviewId, form)
        return success()
    }

    @DeleteMapping("/review/{reviewId}")
    fun deleteReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<Any> {
        reviewService.deleteReview(reviewId)
        return success()
    }

    @GetMapping("/review/{reviewId}")
    fun getReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<ReviewResponse> =
        success(reviewService.getReview(reviewId))


    @PutMapping("/review/{reviewId}/like")
    fun likeReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<Any> {
        reviewService.checkLike(reviewId)
        return success()
    }

    @GetMapping("/theme/{themeId}/review/list")
    fun getReviewList(
        @PathVariable("themeId") themeId: Long,
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<ReviewListResponse> = success(reviewService.getReviewList(themeId, page, size, sort))

    @GetMapping("/user/review/list")
    fun getUserReviewList(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<ReviewListResponse> = success(reviewService.getUserReviewList(page, size, sort))

    @GetMapping("/theme/{themeId}/review/popular-emotion")
    fun getPopularEmotion(@PathVariable("themeId") themeId: Long): CommonResponse<PopularEmotionResponse> =
        success(reviewService.getPopularEmotion(themeId))

}