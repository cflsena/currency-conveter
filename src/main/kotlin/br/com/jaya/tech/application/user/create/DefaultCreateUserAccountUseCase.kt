package br.com.jaya.tech.application.user.create

import br.com.jaya.tech.domain.common.exception.ResourceAlreadyCreatedException
import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import br.com.jaya.tech.shared.assert.AssertThrows
import jakarta.inject.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class CreateUserAccountInput(
    val givenName: String,
    val familyName: String,
    val email: String,
)

data class CreateUserAccountOutput(
    val id: String,
)

@Named
class DefaultCreateUserAccountUseCase(
    private val userRepository: UserRepository,
) : CreateUserAccountUseCase {
    private val log: Logger = LoggerFactory.getLogger(DefaultCreateUserAccountUseCase::class.java)

    override fun execute(input: CreateUserAccountInput): CreateUserAccountOutput {
        log.info("Creating user with {}", input)
        validate(input.email)
        val userToCreate =
            User
                .builder()
                .givenName(input.givenName)
                .familyName(input.familyName)
                .email(input.email)
                .build()
        val userCreated = userRepository.save(userToCreate)
        log.info("User with {} created successfully", userCreated)
        return CreateUserAccountOutput(userCreated.id().value())
    }

    private fun validate(userEmail: String) {
        AssertThrows.isFalse(userRepository.existsByEmail(userEmail)) {
            log.error("User cannot be created, e-mail {} already registered", userEmail)
            throw ResourceAlreadyCreatedException.with("Failed to process request. Please, try again later")
        }
    }
}
