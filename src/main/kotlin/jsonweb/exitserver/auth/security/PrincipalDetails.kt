package jsonweb.exitserver.auth.security

import jsonweb.exitserver.common.USER_NOT_FOUND
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

class PrincipalDetails(private val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add { user.role.toString() }
        return authorities
    }
    override fun getPassword(): String = user.password
    // 유저를 식별할 수 있는 고유값을 넘겨줘야 함 (DB PK)
    override fun getUsername(): String = user.id.toString()
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

@Service
class PrincipalDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(userId: String) = PrincipalDetails(
        userRepository.findById(userId.toLong())
            .orElseThrow { UsernameNotFoundException(USER_NOT_FOUND) }
    )
}

// principal 에는 username (User PK 값) 이 문자열로 들어있다
fun getCurrentLoginUserId() = (SecurityContextHolder
        .getContext()
        .authentication
        .principal as String)
        .toLong()
