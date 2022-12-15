package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.entity.Cafe
import org.springframework.data.jpa.repository.JpaRepository

interface ThemeRepository: JpaRepository<Theme, Long> {
    fun findAllByCafe(cafe: Cafe): List<Theme>
}