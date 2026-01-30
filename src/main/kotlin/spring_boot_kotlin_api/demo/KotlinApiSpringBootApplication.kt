package spring_boot_kotlin_api.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import spring_boot_kotlin_api.demo.config.JwtProperties

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class KotlinApiSpringBootApplication

fun main(args: Array<String>) {
	runApplication<KotlinApiSpringBootApplication>(*args)
}
