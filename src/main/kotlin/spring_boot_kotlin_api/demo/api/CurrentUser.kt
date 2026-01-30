package spring_boot_kotlin_api.demo.api

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

fun currentUserId(): UUID {
    val principal = SecurityContextHolder.getContext().authentication?.principal
        ?: throw IllegalStateException("No authenticated user")
    return UUID.fromString(principal.toString())
}

fun Authentication?.userIdOrNull(): UUID? =
    this?.principal?.toString()?.let { runCatching { UUID.fromString(it) }.getOrNull() }
