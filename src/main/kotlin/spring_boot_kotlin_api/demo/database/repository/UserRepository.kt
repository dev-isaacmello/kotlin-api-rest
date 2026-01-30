package spring_boot_kotlin_api.demo.database.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring_boot_kotlin_api.demo.database.model.User
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
}
