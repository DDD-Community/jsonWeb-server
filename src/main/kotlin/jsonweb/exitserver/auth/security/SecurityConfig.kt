package jsonweb.exitserver.auth.security

import jsonweb.exitserver.auth.jwt.JwtAccessDeniedHandler
import jsonweb.exitserver.auth.jwt.JwtAuthenticationEntryPoint
import jsonweb.exitserver.auth.jwt.JwtFilter
import jsonweb.exitserver.auth.jwt.JwtProvider
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.StrictHttpFirewall

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

    @Bean
    fun setDefaultFirewall(): StrictHttpFirewall {
        val firewall = StrictHttpFirewall()
        firewall.setAllowSemicolon(true)
        firewall.setAllowUrlEncodedDoubleSlash(true)
        return firewall
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .headers().frameOptions().sameOrigin()
        .and() // Spring Security 기반 세션 저장소 사용 안 함
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and() // JWT 401 예외처리 Entry Point 설정
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .and() // JWT Filter 를 Spring Security Filter Chain 에 등록
        .apply(JwtSecurityConfig(jwtProvider))
        .and()
        .authorizeRequests()
        .antMatchers(
            "/users/login", "/users/test-login",
            "/api", "/admin/**", "/images/**", "/admin-page/**", "/img/**"
        ).permitAll()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        // Swagger 설정
        .antMatchers(
            "/docs/**",
            "/favicon.ico",
            "/v2/api-docs",
            "/v3/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/#",
            "/webjars/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/",
            "/csrf",
            "/error"
        ).permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .build()

}

class JwtSecurityConfig(private val jwtProvider: JwtProvider) :
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(
            JwtFilter(jwtProvider),
            UsernamePasswordAuthenticationFilter::class.java
        )
    }
}