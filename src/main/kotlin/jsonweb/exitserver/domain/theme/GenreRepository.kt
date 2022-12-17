package jsonweb.exitserver.domain.theme

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GenreRepository: JpaRepository<Genre, Long> {
    fun findGenreByGenreName(genreName: String): Optional<Genre>
}