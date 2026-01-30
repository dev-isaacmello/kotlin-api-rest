package spring_boot_kotlin_api.demo.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/")
class StatusController {

    data class StatusResponse(
        val status: String = "ok",
        val timestamp: Instant = Instant.now(),
        val message: String = "API is running"
    )

    @GetMapping
    fun getStatus(): StatusResponse = StatusResponse()
}
