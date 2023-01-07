package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class BoastController(private val boastService: BoastService) {
    @GetMapping("/boasts")
    fun getAllBoasts(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int,
    ): CommonResponse<BoastListResponse> = success(boastService.getAllBoasts(sort, page, size))

    @GetMapping("/users/me/boasts")
    fun getUserBoasts(
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<BoastListResponse> = success(boastService.getUserBoasts(sort, page, size))

    @GetMapping("/themes/{themeId}/boasts")
    fun getThemeBoasts(
        @PathVariable("themeId") themeId: Long,
        @RequestParam(defaultValue = "DATE", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<BoastListResponse> = success(boastService.getThemeBoasts(themeId, sort, page, size))

    @PostMapping("/boasts")
    fun createBoast(@Valid @RequestBody form: BoastRequest): CommonResponse<Any> {
        boastService.createBoast(form)
        return success()
    }

    @PutMapping("/boasts/{boastId}")
    fun updateBoast(
        @PathVariable("boastId") id: Long,
        @Valid @RequestBody form: BoastRequest
    ): CommonResponse<Any> {
        boastService.updateBoast(id, form)
        return success()
    }

    @DeleteMapping("/boasts/{boastId}")
    fun deleteBoast(@PathVariable("boastId") id: Long): CommonResponse<Any> {
        boastService.deleteBoast(id)
        return success()
    }

    @PutMapping("/boasts/{boastId}/likes")
    fun likeBoast(@PathVariable("boastId") id: Long): CommonResponse<Any> {
        boastService.checkLike(id)
        return success()
    }

    @PostMapping("/boasts/{boastId}/reports")
    fun reportBoast(
        @PathVariable("boastId") id: Long,
        @RequestBody form: ReportBoastRequest
    ): CommonResponse<Any> {
        boastService.reportBoast(id, form)
        return success()
    }
}
