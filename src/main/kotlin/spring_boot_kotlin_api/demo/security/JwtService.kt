package spring_boot_kotlin_api.demo.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import spring_boot_kotlin_api.demo.config.JwtProperties
import java.util.Base64
import javax.crypto.SecretKey

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {
    val refreshTokenValidityMs: Long get() = jwtProperties.refreshTokenValidityMs

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secret))
    }

    fun generateAccessToken(userId: String): String =
        buildToken(userId, TokenType.ACCESS, jwtProperties.accessTokenValidityMs)

    fun generateRefreshToken(userId: String): String =
        buildToken(userId, TokenType.REFRESH, jwtProperties.refreshTokenValidityMs)

    fun validateAccessToken(token: String): Boolean =
        parseClaims(token)?.let { it.tokenType == TokenType.ACCESS } ?: false

    fun validateRefreshToken(token: String): Boolean =
        parseClaims(token)?.let { it.tokenType == TokenType.REFRESH } ?: false

    fun getUserIdFromToken(token: String): String =
        parseClaims(token)?.subject
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.")

    private fun buildToken(userId: String, type: TokenType, expiryMs: Long): String {
        val now = java.util.Date()
        val expiry = java.util.Date(now.time + expiryMs)
        return Jwts.builder()
            .subject(userId)
            .claim(CLAIM_TYPE, type.value)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun parseClaims(token: String): TokenClaims? {
        val raw = token.removePrefix(BEARER_PREFIX).ifBlank { token }
        return try {
            val payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(raw)
                .payload
            TokenClaims(
                subject = payload.subject,
                tokenType = (payload[CLAIM_TYPE] as? String)?.let { TokenType.from(it) }
            )
        } catch (_: JwtException) {
            null
        }
    }

    private data class TokenClaims(
        val subject: String,
        val tokenType: TokenType?
    )

    private enum class TokenType(val value: String) {
        ACCESS("access"),
        REFRESH("refresh");

        companion object {
            fun from(value: String): TokenType? = entries.find { it.value == value }
        }
    }

    private companion object {
        const val BEARER_PREFIX = "Bearer "
        const val CLAIM_TYPE = "type"
    }
}
