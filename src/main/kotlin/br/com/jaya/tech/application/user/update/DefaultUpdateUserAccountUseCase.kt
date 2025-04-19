package br.com.jaya.tech.application.user.update

import br.com.jaya.tech.domain.common.exception.NotFoundException
import br.com.jaya.tech.domain.common.exception.ResourceAlreadyCreatedException
import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import jakarta.inject.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class UpdateUserAccountInput(
    val id: String,
    val givenName: String,
    val familyName: String,
    val email: String?,
)

@Named
class DefaultUpdateUserAccountUseCase(
    private val userRepository: UserRepository,
) : UpdateUserAccountUseCase {
    private val log: Logger = LoggerFactory.getLogger(DefaultUpdateUserAccountUseCase::class.java)

    override fun execute(input: UpdateUserAccountInput) {
        log.info("Updating user with {}", input)
        val userToUpdate = validateAndGetUserToUpdate(input)
        userToUpdate.updateName(input.givenName, input.familyName)
        if (!input.email.isNullOrBlank()) {
            userToUpdate.updateEmail(input.email)
        }
        val userCreated = userRepository.save(userToUpdate)
        log.info("User with {} updated successfully", userCreated)
    }

    private fun validateAndGetUserToUpdate(input: UpdateUserAccountInput): User {
        val userFound =
            userRepository.findById(input.id) ?: run {
                log.error("User cannot be updated, id {} not found", input.id)
                throw NotFoundException.with("Failed to process request. Please, try again later")
            }
        if (!input.email.isNullOrBlank() &&
            userFound.email.value != input.email &&
            userRepository.existsByEmail(input.email)
        ) {
            log.error("User cannot be updated, e-mail {} already registered", input.email)
            throw ResourceAlreadyCreatedException.with("Failed to process request. Please, try again later")
        }
        return userFound
    }
}
