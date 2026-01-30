package spring_boot_kotlin_api.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    @DefaultValue("15") val accessTokenValidityMinutes: Long = 15L,
    @DefaultValue("43200") val refreshTokenValidityMinutes: Long = 30L * 24 * 60 // 30 days
) {
    val accessTokenValidityMs: Long get() = accessTokenValidityMinutes * 60 * 1000
    val refreshTokenValidityMs: Long get() = refreshTokenValidityMinutes * 60 * 1000
}
