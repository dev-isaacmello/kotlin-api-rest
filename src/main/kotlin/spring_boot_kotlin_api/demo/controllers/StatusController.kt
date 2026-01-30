package spring_boot_kotlin_api.demo.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class StatusController {

    @GetMapping
    fun getStatus(): String = "Everything cool!"
}