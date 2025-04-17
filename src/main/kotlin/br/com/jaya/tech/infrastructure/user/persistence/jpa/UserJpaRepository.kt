package br.com.jaya.tech.infrastructure.user.persistence.jpa

import br.com.jaya.tech.infrastructure.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, String> {
    fun existsByEmail(email: String): Boolean
}