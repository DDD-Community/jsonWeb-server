package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.CafeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Comparator
import org.modelmapper.ModelMapper
import javax.persistence.EntityNotFoundException

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
        val cafe = cafeRepository.findById(form.cafeId).orElseThrow { throw EntityNotFoundException() }
        val theme = modelMapper.map(ThemeWithCafe(form, cafe), Theme::class.java)
        addThemeGenre(theme, form.genreList)
        return themeRepository.save(theme).themeId
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

    fun getThemeList(cafeId: Long): ThemeListResponse {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        val themes = themeRepository.findAllByCafe(cafe)
        sortTheme(themes)
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

    fun sortTheme(themes: List<Theme>){
        themes.sortedWith(kotlin.Comparator { o1, o2 -> (o2.avgStar.toInt() * o2.reviewCount) - (o1.avgStar.toInt() * o1.reviewCount)})
    }

    private fun addGenre(genreList: List<String>) {
        for (genreName in genreList) {
            genreRepository.save(Genre(genreName))
        }
    }

    private fun addThemeGenre(theme: Theme, genreList: List<String>) {
        for (genreName in genreList) {
            val genre = genreRepository.findGenreByGenreName(genreName)
                .orElse(addGenre(genreName))
            val themeGenre = themeGenreRepository.save(ThemeGenre(theme, genre))
            theme.addThemeGenre(themeGenre)
        }
    }

    private fun getTheme(themeId: Long): Theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }

    private fun addGenre(genreName: String): Genre = genreRepository.save(Genre(genreName))
}