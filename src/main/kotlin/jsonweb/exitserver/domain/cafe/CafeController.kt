package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cafe")
class CafeController(private val cafeService: CafeService) {
    @GetMapping("/{cafeId}")
    fun getCafeSpec(@PathVariable("cafeId") cafeId: Long): CommonResponse<CafeSpecResponse> =
        success(cafeService.getCafeSpec(cafeId))

    @GetMapping("/{cafeId}/wrong")
    fun reportWrongCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.markCafeWrong(cafeId)
        return success(null)
    }

    @GetMapping("/{cafeId}/right")
    fun resolveWrongCafe(@PathVariable("cafeId") cafeId: Long) : CommonResponse<Any> {
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
        return success(cafeService.getCafeList(page, size, sort))
    }

//    @PostMapping("/new")
//    fun uploadNewCafe(): CommonResponse<> {
//
//    }

}