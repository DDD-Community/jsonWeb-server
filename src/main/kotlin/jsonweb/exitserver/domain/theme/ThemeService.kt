package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.CafeRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ThemeService(
    private val cafeRepository: CafeRepository,
    private val themeRepository: ThemeRepository,
    private val genreRepository: GenreRepository,
    private val themeGenreRepository: ThemeGenreRepository,
    private val modelMapper: ModelMapper
) {
    @Transactional
    fun registerTheme(form: RegisterThemeRequest): Long {
        val cafe = cafeRepository.findById(form.cafeId).orElseThrow()
        val theme = themeRepository.save(
            modelMapper.map(ThemeWithCafe(form, cafe), Theme::class.java)
        )
        cafe.addTheme(theme)
        addThemeGenre(theme, form.genreList)
        return theme.themeId
    }

    @Transactional
    fun registerThemeGenre(form: RegisterThemeGenreRequest) {
        val theme = getTheme(form.themeId)
        addThemeGenre(theme, form.genreList)
    }

    @Transactional
    fun deleteTheme(themeId: Long) {
        themeRepository.deleteById(themeId)
    }

    fun getThemeList(cafeId: Long, sort: String): ThemeListResponse {
        val cafe = cafeRepository.findById(cafeId).orElseThrow()
        val themes = themeRepository.findAllByCafe(cafe)
        if (sort == "POPULAR") {
            sortTheme(themes)
        } else {
            themes.sortedBy { it.name }
        }
        return ThemeListResponse(themes.map { ThemeResponse(it) })
    }

    fun getThemeSpec(themeId: Long): ThemeSpecResponse {
        val theme = getTheme(themeId)
        return ThemeSpecResponse(theme)
    }

    @Transactional
    fun updateTheme(themeId: Long, form: UpdateThemeRequest) {
        val theme = getTheme(themeId)
        // TODO: update 로직 어떻게 처리할지 고민
    }

    fun sortTheme(themes: List<Theme>) {
        themes.sortedWith { o1, o2 -> (o2.avgStar.toInt() * o2.reviewCount) - (o1.avgStar.toInt() * o1.reviewCount) }
    }

    private fun addThemeGenre(theme: Theme, genreList: List<String>) {
        for (genreName in genreList) {
            val genre = genreRepository.findGenreByGenreName(genreName)
                .orElse(genreRepository.save(Genre(genreName)))
            val themeGenre = themeGenreRepository.save(ThemeGenre(theme, genre))
            theme.addThemeGenre(themeGenre)
        }
    }

    private fun getTheme(themeId: Long): Theme = themeRepository.findById(themeId).orElseThrow()

    fun getThemeGenreList() = GenreEnum.values()
        .map { it.kor() }
        .toList()

}