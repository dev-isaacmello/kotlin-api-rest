package spring_boot_kotlin_api.demo.database.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "user_id", nullable = false)
    val userId: UUID,
    @Column(name = "expires_at", nullable = false)
    val expiresAt: Instant,
    @Column(name = "hashed_token", nullable = false)
    val hashedToken: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
)
