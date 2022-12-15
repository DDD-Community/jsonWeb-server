package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.CafeRepository
import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.cafe.entity.CafeReport
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class ThemeService(
    private val cafeRepository: CafeRepository,
    private val themeRepository: ThemeRepository,
    private val genreRepository: GenreRepository
) {
    @Transactional
    fun registerTheme(form: RegisterThemeRequest): Long {
        val cafe = cafeRepository.findById(form.cafeId).orElseThrow { throw EntityNotFoundException() }
        val theme = makeTheme(
            form.name,
            form.description,
            form.imageUrl,
            form.time,
            form.minPlayerCount,
            form.maxPlayerCount,
            form.difficulty,
            form.ageLimit,
            cafe
        )
        return themeRepository.save(theme).themeId
    }

    @Transactional
    fun registerThemeGenre(form: RegisterThemeGenreRequest) {
        val theme = themeRepository.findById(form.themeId).orElseThrow { throw EntityNotFoundException() }
        for (genreName in form.genreList) {
            val genre = genreRepository.findGenreByGenreName(genreName)
                .orElse(genreRepository.save(Genre(genreName)))
            theme.addGenre(genre)
        }
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
        val theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }
        return ThemeSpecResponse(theme)
    }

    @Transactional
    fun updateTheme(themeId: Long, form: UpdateThemeRequest) {
        val theme = themeRepository.findById(themeId).orElseThrow { throw EntityNotFoundException() }
        // TODO: update 로직 어떻게 처리할지 고민
    }

    fun sortTheme(themeList: List<Theme>): List<Theme> {
        // TODO: 정렬 알고리즘 고안
        return themeList
    }

    private fun makeTheme(
        name: String,
        description: String,
        imageUrl: String,
        time: Int,
        minPlayerCount: Int,
        maxPlayerCount: Int,
        difficulty: Double,
        ageLimit: String,
        cafe: Cafe
    ): Theme = Theme(name, description, imageUrl, time, minPlayerCount, maxPlayerCount, difficulty, ageLimit, cafe)
}