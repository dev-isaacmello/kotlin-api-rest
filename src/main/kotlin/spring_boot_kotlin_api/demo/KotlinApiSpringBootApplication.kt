package spring_boot_kotlin_api.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinApiSpringBootApplication

fun main(args: Array<String>) {
	runApplication<KotlinApiSpringBootApplication>(*args)
}
