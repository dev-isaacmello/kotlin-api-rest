package spring_boot_kotlin_api.demo.controllers

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring_boot_kotlin_api.demo.api.TokenPairResponse
import spring_boot_kotlin_api.demo.api.UserResponse
import spring_boot_kotlin_api.demo.security.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    data class AuthRequest(
        @field:Email(message = "Invalid email")
        val email: String,
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
            message = "Password must be at least 9 characters with one digit, one uppercase and one lowercase letter."
        )
        val password: String
    )

    data class RefreshRequest(
        @field:NotBlank(message = "Refresh token is required")
        val refreshToken: String
    )

    @PostMapping("/register")
    fun register(@Valid @RequestBody body: AuthRequest): UserResponse {
        val user = authService.register(body.email, body.password)
        return UserResponse(id = user.id.toHexString(), email = user.email)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: AuthRequest): TokenPairResponse {
        val pair = authService.login(body.email, body.password)
        return TokenPairResponse(accessToken = pair.accessToken, refreshToken = pair.refreshToken)
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody body: RefreshRequest): TokenPairResponse {
        val pair = authService.refresh(body.refreshToken)
        return TokenPairResponse(accessToken = pair.accessToken, refreshToken = pair.refreshToken)
    }
}
