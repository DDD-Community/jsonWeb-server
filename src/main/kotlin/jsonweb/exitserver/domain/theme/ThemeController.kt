package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/theme")
class ThemeController(
    private val themeService: ThemeService
) {
    @PostMapping
    fun registerTheme(@RequestBody form: RegisterThemeRequest): CommonResponse<Long> =
        success(themeService.registerTheme(form))

    @PostMapping("/genre")
    fun registerThemeGenre(@RequestBody form: RegisterThemeGenreRequest): CommonResponse<Any> {
        themeService.registerThemeGenre(form)
        return success(null)
    }

    @GetMapping("/{themeId}")
    fun getThemeSpec(@PathVariable themeId: Long): CommonResponse<ThemeSpecResponse> =
        success(themeService.getThemeSpec(themeId))

//    @PutMapping("/{themeId}")
//    fun updateTheme(
//        @PathVariable themeId: Long,
//        @RequestBody form: UpdateThemeRequest
//    ): CommonResponse<Any> {
//
//    }

    @DeleteMapping("/{themeId}")
    fun deleteTheme(@PathVariable themeId: Long): CommonResponse<Any> {
        themeService.deleteTheme(themeId)
        return success(null)
    }

    @GetMapping("/list")
    fun getThemeList(@RequestParam(required = true) cafeId: Long): CommonResponse<ThemeListResponse> =
        success(themeService.getThemeList(cafeId))

}