package jsonweb.exitserver.domain.review

import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ReviewRepository: JpaRepository<Review, Long> {

    @Query("select r from Review r join fetch r.user u join fetch r.theme t join fetch t.cafe c where r.reviewId = :id")
    override fun findById(id: Long): Optional<Review>

    @Query("select r from Review r join fetch r.user u join fetch r.theme t join fetch t.cafe")
    fun findAllByTheme(theme: Theme): List<Review>
    fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Review>
    fun findAllByUser(user: User, pageable: Pageable): Page<Review>
}

interface ReviewLikeRepository: JpaRepository<ReviewLike, UserAndReview> {
    fun findAllByUserId(userId: Long): List<ReviewLike>
}