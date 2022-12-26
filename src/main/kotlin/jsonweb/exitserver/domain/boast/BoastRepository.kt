package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BoastRepository : JpaRepository<Boast, Long> {
    @Query("SELECT b FROM Boast b WHERE b.visibility = true")
    override fun findAll(pageable: Pageable): Page<Boast>

    @Query("SELECT b FROM Boast b WHERE b.user = :user AND b.visibility = true")
    fun findAllByUser(user: User, pageable: Pageable): Page<Boast>

    @Query("SELECT b FROM Boast b WHERE b.theme = :theme AND b.visibility = true")
    fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Boast>
}

interface BoastLikeRepository : JpaRepository<BoastLike, BoastLikeId> {
}

interface BoastReportRepository : JpaRepository<BoastReport, Long> {
}