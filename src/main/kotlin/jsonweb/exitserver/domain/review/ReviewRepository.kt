package jsonweb.exitserver.domain.review

import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Long> {
    fun findAllByTheme(theme: Theme): List<Review>
    fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Review>
    fun findAllByUser(user: User, pageable: Pageable): Page<Review>
}