package spring_boot_kotlin_api.demo.api

data class TokenPairResponse(
    val accessToken: String,
    val refreshToken: String
)
