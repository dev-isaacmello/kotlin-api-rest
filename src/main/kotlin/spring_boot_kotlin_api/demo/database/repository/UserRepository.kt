package spring_boot_kotlin_api.demo.database.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import spring_boot_kotlin_api.demo.database.model.User

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}