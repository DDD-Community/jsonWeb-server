package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.CafeRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
        val themeList = themeRepository.findAllByCafe(cafe).map { ThemeResponse(it) }
        // TODO: 정렬 알고리즘 추가
        return ThemeListResponse(themeList)
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

    fun sortTheme(themeList: List<Theme>): List<Theme> {
        // TODO: 정렬 알고리즘 고안
        return themeList
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

    private fun getTheme(themeId: Long): Theme = themeRepository.findById(form.themeId).orElseThrow { throw EntityNotFoundException() }

    private fun addGenre(genreName: String): Genre = genreRepository.save(Genre(genreName))
}