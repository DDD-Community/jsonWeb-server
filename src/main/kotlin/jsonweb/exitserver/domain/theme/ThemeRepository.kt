package jsonweb.exitserver.domain.theme

import org.springframework.data.jpa.repository.JpaRepository

interface ThemeRepository: JpaRepository<Theme, Long> {
}