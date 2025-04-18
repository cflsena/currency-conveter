package br.com.jaya.tech.infrastructure.user.api

import br.com.jaya.tech.infrastructure.common.api.ApiBaseDocumentation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("api/v1/jaya-tech/users")
@Tag(name = "User", description = "API to manage users")
fun interface UserApi : ApiBaseDocumentation {

    @ApiResponse(responseCode = "201", description = "Created successfully")
    @Operation(summary = "Endpoint to create user account")
    @PostMapping(
        "/accounts",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUserAccount(@RequestBody request: CreateUserAccountRequest): ResponseEntity<UserAccountResponse>

}