package br.com.jaya.tech.domain.user

interface UserRepository {
    fun save(user: User) : User
    fun existsByEmail(email: String): Boolean
}