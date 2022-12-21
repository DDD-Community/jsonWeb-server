package jsonweb.exitserver.domain.review

import org.springframework.data.jpa.repository.JpaRepository

interface ReviewLikeRepository: JpaRepository<ReviewLike, UserAndReview> {
    fun findAllByUserId(userId: Long): List<ReviewLike>
}