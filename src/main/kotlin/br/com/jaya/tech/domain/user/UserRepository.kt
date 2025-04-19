package br.com.jaya.tech.domain.user

import java.util.*

interface UserRepository {
    fun save(user: User): User

    fun existsByEmail(email: String): Boolean

    fun findById(id: UUID): User?

    fun existsById(id: UUID): Boolean
}
