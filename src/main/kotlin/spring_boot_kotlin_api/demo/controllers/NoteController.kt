package spring_boot_kotlin_api.demo.controllers

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import spring_boot_kotlin_api.demo.api.currentUserId
import spring_boot_kotlin_api.demo.database.model.Note
import spring_boot_kotlin_api.demo.database.repository.NoteRepository
import java.time.Instant

@RestController
@RequestMapping("/notes")
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
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
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
        val objectId = ObjectId(id)
        val note = noteRepository.findById(objectId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found")
        }
        if (note.ownerId != currentUserId()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this note")
        }
        noteRepository.deleteById(objectId)
    }

    private fun Note.toResponse() = NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt
    )
}
