package br.com.jaya.tech.infrastructure.user.api

import br.com.jaya.tech.application.user.create.CreateUserAccountUseCase
import br.com.jaya.tech.infrastructure.user.mapper.UserMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

data class CreateUserAccountRequest(
    val givenName: String,
    val familyName: String,
    val email: String
)

data class UserAccountResponse(
    val id: String
)

@RestController
class UserController(private val createUserAccountUseCase: CreateUserAccountUseCase) : UserApi {

    override fun createUserAccount(request: CreateUserAccountRequest): ResponseEntity<UserAccountResponse> {
        val userCreated = createUserAccountUseCase.execute(UserMapper.toInput(request))
        return ResponseEntity.status(HttpStatus.CREATED).body(UserAccountResponse(userCreated.id))
    }

}
