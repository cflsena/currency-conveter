package br.com.jaya.tech.infrastructure.user.mapper

import br.com.jaya.tech.application.user.create.CreateUserAccountInput
import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.infrastructure.user.api.CreateUserAccountRequest
import br.com.jaya.tech.infrastructure.user.persistence.UserEntity

object UserMapper {
    fun toEntity(user: User): UserEntity =
        UserEntity
            .builder()
            .id(user.id().value())
            .givenName(user.name.givenName)
            .familyName(user.name.familyName)
            .email(user.email.value)
            .build()

    fun toDomain(user: UserEntity): User =
        User
            .builder()
            .id(user.id)
            .givenName(user.givenName)
            .familyName(user.familyName)
            .email(user.email)
            .build()

    fun toInput(request: CreateUserAccountRequest): CreateUserAccountInput =
        CreateUserAccountInput(request.givenName, request.familyName, request.email)
}
