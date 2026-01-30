package spring_boot_kotlin_api.demo.api

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.bson.types.ObjectId

/**
 * Extracts the authenticated user ID from the security context.
 * Use in controllers instead of raw SecurityContextHolder access.
 */
fun currentUserId(): ObjectId {
    val principal = SecurityContextHolder.getContext().authentication?.principal
        ?: throw IllegalStateException("No authenticated user")
    return ObjectId(principal.toString())
}

fun Authentication?.userIdOrNull(): ObjectId? =
    this?.principal?.toString()?.let { ObjectId(it) }
