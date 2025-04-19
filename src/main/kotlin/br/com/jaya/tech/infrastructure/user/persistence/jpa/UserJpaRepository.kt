package br.com.jaya.tech.infrastructure.user.persistence.jpa

import br.com.jaya.tech.infrastructure.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    fun existsByEmail(email: String): Boolean
}
