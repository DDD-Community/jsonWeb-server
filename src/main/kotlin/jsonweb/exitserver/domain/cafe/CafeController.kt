package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
class CafeController(private val cafeService: CafeService) {
    @PostMapping("/cafe")
    fun registerCafe(@RequestBody form: RegisterCafeRequest): CommonResponse<Any> =
        success(cafeService.registerCafe(form))

    @DeleteMapping("/cafe/{cafeId}")
    fun deleteCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.deleteCafe(cafeId)
        return success()
    }

    @GetMapping("/cafe/{cafeId}")
    fun getCafeSpec(@PathVariable("cafeId") cafeId: Long): CommonResponse<CafeSpecResponse> =
        success(cafeService.getCafeSpec(cafeId))

    @PutMapping("/cafe/{cafeId}/like")
    fun likeCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.checkLike(cafeId)
        return success()
    }

    @GetMapping("/cafes")
    fun getCafeList(
        @RequestParam(defaultValue = "DEFAULT", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) genre: String?
    ): CommonResponse<CafeListResponse> {
        keyword?.let {
            return success(cafeService.getCafeListWithKeyword(keyword, page, size, sort))
        } ?: return success(cafeService.getCafeList(genre, page, size, sort))
    }

    @GetMapping("/users/me/cafes")
    fun getLikeCafeList(
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int
    ): CommonResponse<CafeListResponse> = success(cafeService.getLikeCafeList(page, size))

}