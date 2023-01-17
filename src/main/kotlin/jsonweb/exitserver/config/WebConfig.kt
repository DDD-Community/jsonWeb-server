package jsonweb.exitserver.config

import jsonweb.exitserver.util.IpLogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(private val ipLogInterceptor: IpLogInterceptor) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedOrigins("*")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(ipLogInterceptor)
    }
}