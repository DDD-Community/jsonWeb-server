package jsonweb.exitserver.domain.review

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/review")
class ReviewController(
    private val reviewService: ReviewService
) {
    @PostMapping("/{themeId}")
    fun createReview(
        @PathVariable("themeId") themeId: Long,
        @RequestBody form: CreateReviewRequest
    ): CommonResponse<Any> {
        reviewService.createReview(themeId, form)
        return success(null)
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable("reviewId") reviewId: Long,
        @RequestBody form: UpdateReviewRequest
    ): CommonResponse<Any> {
        reviewService.updateReview(reviewId, form)
        return success(null)
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<Any> {
        reviewService.deleteReview(reviewId)
        return success(null)
    }

    @PutMapping("/{reviewId}/like")
    fun likeReview(@PathVariable("reviewId") reviewId: Long): CommonResponse<Any> {
        reviewService.checkLike(reviewId)
        return success(null)
    }

    @GetMapping("/{themeId}")
    fun getReviewList(
        @PathVariable("themeId") themeId: Long,
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<ReviewListResponse> = success(reviewService.getReviewList(themeId, page, size, sort))

    @GetMapping("/user")
    fun getUserReviewList(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<ReviewListResponse> = success(reviewService.getUserReviewList(page, size, sort))

    @GetMapping("/{themeId}/popular-emotion")
    fun getPopularEmotion(@PathVariable("themeId") themeId: Long): CommonResponse<PopularEmotionResponse> =
        success(reviewService.getPopularEmotion(themeId))

}