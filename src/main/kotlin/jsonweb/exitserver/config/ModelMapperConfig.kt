package jsonweb.exitserver.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {
    @Bean
    fun modelMapper() = ModelMapper().apply {
        configuration.isFieldMatchingEnabled = true // 같은 이름의 필드 매칭
        configuration.fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE // private 필드에도 접근 가능
    }
}