package spring_boot_kotlin_api.demo.controllers

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import spring_boot_kotlin_api.demo.api.currentUserId
import spring_boot_kotlin_api.demo.database.model.Note
import spring_boot_kotlin_api.demo.database.repository.NoteRepository
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/notes")
@Tag(name = "Notes", description = "CRUD de notas (requer Bearer token)")
class NoteController(
    private val noteRepository: NoteRepository
) {
    data class NoteRequest(
        val id: String?,
        @field:NotBlank(message = "Title is required")
        val title: String,
        val content: String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
    fun save(@Valid @RequestBody body: NoteRequest): NoteResponse {
        val ownerId = currentUserId()
        val note = noteRepository.save(
            Note(
                id = body.id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
                title = body.title.trim(),
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ownerId
            )
        )
        return note.toResponse()
    }

    @GetMapping
    fun findByOwner(): List<NoteResponse> {
        val ownerId = currentUserId()
        return noteRepository.findByOwnerId(ownerId).map { it.toResponse() }
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String) {
        val noteId = UUID.fromString(id)
        val note = noteRepository.findById(noteId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found")
        }
        if (note.ownerId != currentUserId()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this note")
        }
        noteRepository.deleteById(noteId)
    }

    private fun Note.toResponse() = NoteResponse(
        id = id.toString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt
    )
}
