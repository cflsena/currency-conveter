package br.com.jaya.tech.infrastructure.user.persistence

import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import br.com.jaya.tech.infrastructure.user.mapper.UserMapper
import br.com.jaya.tech.infrastructure.user.persistence.jpa.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class DefaultUserRepository(
    private val repository: UserJpaRepository,
) : UserRepository {
    @Transactional
    override fun save(user: User): User = repository.save(UserMapper.toEntity(user)).let(UserMapper::toDomain)

    override fun existsByEmail(email: String): Boolean = repository.existsByEmail(email)

    override fun findById(id: UUID): User? = repository.findByIdOrNull(id)?.let(UserMapper::toDomain)

    override fun existsById(id: UUID): Boolean = repository.existsById(id)
}
