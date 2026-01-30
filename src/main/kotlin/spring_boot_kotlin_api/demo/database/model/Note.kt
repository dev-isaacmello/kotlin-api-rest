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
@Table(name = "notes")
data class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "owner_id", nullable = false)
    val ownerId: UUID,
    @Column(nullable = false)
    val title: String,
    @Column(nullable = false, columnDefinition = "text")
    val content: String,
    @Column(nullable = false)
    val color: Long,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
)
