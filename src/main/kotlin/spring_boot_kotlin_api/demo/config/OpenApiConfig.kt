package spring_boot_kotlin_api.demo.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Kotlin API Spring Boot")
                .description("API REST com autenticação JWT, usuários e notas. Use /auth/login para obter o token e autorize com Bearer token nas rotas protegidas.")
                .version("1.0.0")
        )
        .addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
        .components(
            Components()
                .addSecuritySchemes(
                    "Bearer Authentication",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer")
                        .description("Informe o accessToken retornado por POST /auth/login")
                )
        )
}
