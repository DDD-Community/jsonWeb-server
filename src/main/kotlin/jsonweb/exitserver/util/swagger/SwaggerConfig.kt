package jsonweb.exitserver.util.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {
    @Bean
    fun swagger(): Docket = Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
        .forCodeGeneration(true)
        .select()
        .apis(RequestHandlerSelectors.basePackage("jsonweb.exitserver"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .enable(true)

    private fun apiInfo(): ApiInfo = ApiInfoBuilder()
        .title("EXIT API SPEC")
        .description("EXIT API SPEC")
        .build()

}