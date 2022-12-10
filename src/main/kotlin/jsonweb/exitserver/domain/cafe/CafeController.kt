package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cafe")
class CafeController(private val cafeService: CafeService) {
    @PostMapping
    fun registerCafe(@RequestBody form: RegisterCafeRequest): CommonResponse<Any> {
        cafeService.registerCafe(form)
        return success(null)
    }

    @DeleteMapping("/{cafeId}")
    fun deleteCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.deleteCafe(cafeId)
        return success(null)
    }

    @GetMapping("/{cafeId}")
    fun getCafeSpec(@PathVariable("cafeId") cafeId: Long): CommonResponse<CafeSpecResponse> =
        success(cafeService.getCafeSpec(cafeId))

    @GetMapping("/{cafeId}/like")
    fun likeCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.likeCafe(cafeId)
        return success(null)
    }

    @GetMapping("/{cafeId}/unlike")
    fun unlikeCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.unlikeCafe(cafeId)
        return success(null)
    }

    @GetMapping("/{cafeId}/wrong")
    fun reportWrongCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.markCafeWrong(cafeId)
        return success(null)
    }

    @GetMapping("/{cafeId}/right")
    fun resolveWrongCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.markCafeRight(cafeId)
        return success(null)
    }

    @GetMapping("/list")
    fun getCafeList(
        @RequestParam(defaultValue = "DEFAULT", required = false) sort: String,
        @RequestParam(defaultValue = "0", required = false) page: Int,
        @RequestParam(defaultValue = "16", required = false) size: Int,
        @RequestParam(required = false) keyword: String?
    ): CommonResponse<CafeListResponse> {
        keyword?.let {
            return success(cafeService.getCafeListWithKeyword(keyword, page, size, sort))
        } ?: return success(cafeService.getCafeList(page, size, sort))
    }

}