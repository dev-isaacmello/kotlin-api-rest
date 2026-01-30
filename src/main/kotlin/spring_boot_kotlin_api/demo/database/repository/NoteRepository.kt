package spring_boot_kotlin_api.demo.database.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring_boot_kotlin_api.demo.database.model.Note
import java.util.UUID

interface NoteRepository : JpaRepository<Note, UUID> {
    fun findByOwnerId(ownerId: UUID): List<Note>
}
