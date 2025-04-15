package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.utils.IdUtils

data class UserId(val value: String) {
    companion object {
        fun create(): UserId {
            return UserId(IdUtils.generate())
        }
        fun create(id: String): UserId {
            return UserId(id)
        }
    }
}

class User private constructor(
    val id: UserId,
    val name: Name,
    val email: Email
) {

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {

        private var id: String? = null
        private var givenName: String? = null
        private var familyName: String? = null
        private var email: String? = null

        fun id(id: String) = apply { this.id = id }
        fun givenName(givenName: String) = apply { this.givenName = givenName }
        fun familyName(familyName: String) = apply { this.familyName = familyName }
        fun email(email: String) = apply { this.email = email }

        fun build(): User {
            val id = if (this.id == null) UserId.create() else UserId.create(this.id!!);
            val name = Name.of(this.givenName, this.familyName)
            val email = Email.of(this.email)
            return User(id, name, email)
        }

    }

}