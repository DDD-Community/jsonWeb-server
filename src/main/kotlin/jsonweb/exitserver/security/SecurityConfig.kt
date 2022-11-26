package jsonweb.exitserver.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .headers().frameOptions().sameOrigin().and()

        // 엔드포인트 Auth 설정
//        http.authorizeHttpRequests()
//            .antMatchers("/**").permitAll().and()

        return http.build()
    }

}