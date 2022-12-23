package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.cafe.entity.CafeLike
import jsonweb.exitserver.domain.cafe.entity.UserAndCafe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CafeLikeRepository: JpaRepository<CafeLike, UserAndCafe> {
    fun findAllByUserId(userId: Long): List<CafeLike>
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<CafeLike>
}