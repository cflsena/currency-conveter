package br.com.jaya.tech.infrastructure.user.api

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("api/v1/jaya-tech/users")
fun interface UserApi {
    @PostMapping("/accounts")
    fun createUserAccount(@Valid @RequestBody request: CreateUserAccountRequest) : ResponseEntity<UserAccountResponse>
}