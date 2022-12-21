package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cafe")
class CafeController(private val cafeService: CafeService) {
    @PostMapping
    fun registerCafe(@RequestBody form: RegisterCafeRequest): CommonResponse<Long> =
        success(cafeService.registerCafe(form))

    @DeleteMapping("/{cafeId}")
    fun deleteCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.deleteCafe(cafeId)
        return success(null)
    }

    @GetMapping("/{cafeId}")
    fun getCafeSpec(@PathVariable("cafeId") cafeId: Long): CommonResponse<CafeSpecResponse> =
        success(cafeService.getCafeSpec(cafeId))

    @PutMapping("/{cafeId}/like")
    fun likeCafe(@PathVariable("cafeId") cafeId: Long): CommonResponse<Any> {
        cafeService.checkLike(cafeId)
        return success(null)
    }

    @GetMapping("/list")
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

//    @PostMapping("/{cafeId}/report")
//    fun reportCafe(
//        @PathVariable("cafeId") cafeId: Long,
//        @RequestBody form: ReportCafeRequest
//    ): CommonResponse<Any> {
//        cafeService.reportCafe(cafeId, form.reportContent)
//        return success(null)
//    }

//    @DeleteMapping("{reportId}/resolve")
//    fun resolveCafe(
//        @PathVariable("reportId") reportId: Long
//    ): CommonResponse<Any> {
//        cafeService.resolveCafe(reportId)
//        return success(null)
//    }

}