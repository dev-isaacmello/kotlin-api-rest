package spring_boot_kotlin_api.demo.security

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import spring_boot_kotlin_api.demo.database.model.RefreshToken
import spring_boot_kotlin_api.demo.database.model.User
import spring_boot_kotlin_api.demo.database.repository.RefreshTokenRepository
import spring_boot_kotlin_api.demo.database.repository.UserRepository
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register(email: String, password: String): User {
        val normalizedEmail = email.trim().lowercase()
        if (userRepository.findByEmail(normalizedEmail) != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User already exists.")
        }
        return userRepository.save(
            User(
                email = normalizedEmail,
                hashedPassword = hashEncoder.encode(password)
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email.trim().lowercase())
            ?: throw BadCredentialsException("Invalid credentials.")
        if (!hashEncoder.matches(password, user.hashedPassword!!)) {
            throw BadCredentialsException("Invalid credentials.")
        }
        val userId = user.id.toHexString()
        val accessToken = jwtService.generateAccessToken(userId)
        val refreshToken = jwtService.generateRefreshToken(userId)
        storeRefreshToken(user.id, refreshToken)
        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token.")
        }
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token.")
        }
        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashed)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not recognized.")
        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashed)

        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)
        storeRefreshToken(user.id, newRefreshToken)
        return TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    private fun storeRefreshToken(userId: ObjectId, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiresAt = Instant.now().plusMillis(jwtService.refreshTokenValidityMs)
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}