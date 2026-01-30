package spring_boot_kotlin_api.demo.database.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring_boot_kotlin_api.demo.database.model.RefreshToken
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUserIdAndHashedToken(userId: UUID, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: UUID, hashedToken: String)
}
