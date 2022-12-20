package jsonweb.exitserver.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun HttpServletRequest.getToken(): String? {
    val rawToken = this.getHeader("Authorization")
    return if (rawToken != null && rawToken.startsWith("Bearer"))
        rawToken.replace("Bearer ", "")
    else null
}

class JwtFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getToken()
        if (jwt != null && jwtProvider.validate(jwt)) {
            SecurityContextHolder.getContext().authentication =
                jwtProvider.getAuthentication(jwt)
        }
        filterChain.doFilter(request, response)
    }
}

