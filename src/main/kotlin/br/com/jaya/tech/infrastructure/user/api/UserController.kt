package br.com.jaya.tech.infrastructure.user.api

import br.com.jaya.tech.application.user.create.CreateUserAccountUseCase
import br.com.jaya.tech.infrastructure.user.mapper.UserMapper
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

data class CreateUserAccountRequest(
    @Schema(description = "First name of user", example = "John", minLength = 3, maxLength = 50, required = true)
    val givenName: String,
    @Schema(description = "Last name of user", example = "Doe", minLength = 3, maxLength = 50, required = true)
    val familyName: String,
    @Schema(description = "User of user", example = "test@test.com", maxLength = 50, required = true)
    val email: String,
)

data class UserAccountResponse(
    @Schema(description = "User id created", example = "a8ac2072-bb41-458f-8a75-aaefbd24a42b")
    val id: String,
)

@RestController
class UserController(
    private val createUserAccountUseCase: CreateUserAccountUseCase,
) : UserApi {
    override fun createUserAccount(request: CreateUserAccountRequest): ResponseEntity<UserAccountResponse> {
        val userCreated = createUserAccountUseCase.execute(UserMapper.toInput(request))
        return ResponseEntity.status(HttpStatus.CREATED).body(UserAccountResponse(userCreated.id))
    }
}
