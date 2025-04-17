package br.com.jaya.tech.infrastructure.user.persistence

import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import br.com.jaya.tech.infrastructure.user.mapper.UserMapper
import br.com.jaya.tech.infrastructure.user.persistence.jpa.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

open class DefaultUserRepository(
    private val repository: UserJpaRepository
) : UserRepository {

    @Transactional
    override fun save(user: User): User = repository.save(UserMapper.toEntity(user)).let(UserMapper::toDomain)

    override fun existsByEmail(email: String): Boolean = repository.existsByEmail(email)

    override fun findById(id: String): User? = repository.findByIdOrNull(id)?.let(UserMapper::toDomain)

    override fun existsById(id: String): Boolean = repository.existsById(id)

}
