package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BoastRepository : JpaRepository<Boast, Long> {
    fun findAllByUser(user: User, pageable: Pageable): Page<Boast>
    fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Boast>
}

interface BoastLikeRepository : JpaRepository<BoastLike, BoastLikeId> {
}