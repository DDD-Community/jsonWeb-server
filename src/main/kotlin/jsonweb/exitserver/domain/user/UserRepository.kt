package jsonweb.exitserver.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>{
    fun existsByNickname(nickname: String): Boolean
    fun findByKakaoId(kakaoId: Long): User?
}
