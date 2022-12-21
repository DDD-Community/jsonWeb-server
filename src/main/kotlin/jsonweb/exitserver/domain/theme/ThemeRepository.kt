package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.Cafe
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ThemeRepository: JpaRepository<Theme, Long> {
    fun findAllByCafe(cafe: Cafe): List<Theme>
}

interface ThemeGenreRepository: JpaRepository<ThemeGenre, Long> {
}

interface GenreRepository: JpaRepository<Genre, Long> {
    fun findGenreByGenreName(genreName: String): Optional<Genre>
}