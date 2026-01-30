package spring_boot_kotlin_api.demo.database.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import spring_boot_kotlin_api.demo.database.model.RefreshToken

interface RefreshTokenRepository: MongoRepository<RefreshToken, ObjectId> {
    fun findByUserIdAndHashedToken(userId: ObjectId, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: ObjectId, hashedToken: String)
}