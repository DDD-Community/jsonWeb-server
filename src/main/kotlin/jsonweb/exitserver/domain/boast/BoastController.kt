package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/boast")
class BoastController(private val boastService: BoastService) {

    // 정렬 기준 : 최신순, 인기순
    @GetMapping
    fun getBoastList(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int,
    ): CommonResponse<BoastListResponse> = success(boastService.getBoastList(sort, page, size))

    // 마지막 수정된 날짜 순
    @GetMapping("/user")
    fun getUserBoastList(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<BoastListResponse> = success(boastService.getUserBoastList(sort, page, size))

    @GetMapping("/theme/{themeId}")
    fun getBoastsByTheme(
        @PathVariable("themeId") themeId: Long,
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<BoastListResponse> = success(boastService.getThemeBoastList(themeId, sort, page, size))

    @PostMapping
    fun createBoast(@RequestBody form: BoastRequest): CommonResponse<Any> {
        boastService.createBoast(form)
        return success()
    }

    @PutMapping("/{boastId}")
    fun updateBoast(
        @PathVariable("boastId") id: Long,
        @RequestBody form: BoastRequest
    ): CommonResponse<Any> {
        boastService.updateBoast(id, form)
        return success()
    }

    @DeleteMapping("/{boastId}")
    fun deleteBoast(@PathVariable("boastId") id: Long): CommonResponse<Any> {
        boastService.deleteBoast(id)
        return success()
    }

    @PutMapping("/{boastId}/like")
    fun likeBoast(@PathVariable("boastId") reviewId: Long): CommonResponse<Any> {
        boastService.checkLike(reviewId)
        return success()
    }
}
