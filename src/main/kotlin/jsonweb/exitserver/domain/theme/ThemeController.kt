package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
class ThemeController(
    private val themeService: ThemeService
) {
    @PostMapping("/theme")
    fun registerTheme(@RequestBody form: RegisterThemeRequest): CommonResponse<Long> =
        success(themeService.registerTheme(form))

    @PostMapping("/theme/genre")
    fun registerThemeGenre(@RequestBody form: RegisterThemeGenreRequest): CommonResponse<Any> {
        themeService.registerThemeGenre(form)
        return success()
    }

    @GetMapping("/theme/{themeId}")
    fun getThemeSpec(@PathVariable themeId: Long): CommonResponse<ThemeSpecResponse> =
        success(themeService.getThemeSpec(themeId))

    @DeleteMapping("/theme/{themeId}")
    fun deleteTheme(@PathVariable themeId: Long): CommonResponse<Any> {
        themeService.deleteTheme(themeId)
        return success()
    }

    @GetMapping("/themes")
    fun getThemeList(
        @RequestParam(required = true) cafeId: Long,
        @RequestParam(required = true) sort: String
    ): CommonResponse<ThemeListResponse> =
        success(themeService.getThemeList(cafeId, sort))
}