package br.com.jaya.tech.infrastructure.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "tb_user")
class UserEntity(

    @Id
    @NotNull
    @NotBlank
    @Column(name = "id", length = 36, nullable = false)
    val id: String,

    @NotNull
    @NotBlank
    @Column(name = "given_name", nullable = false)
    val givenName: String,

    @NotNull
    @NotBlank
    @Column(name = "family_name", nullable = false)
    val familyName: String,

    @NotNull
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

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

        fun build(): UserEntity {
            return UserEntity(
                this.id!!,
                this.givenName ?: "",
                this.familyName ?: "",
                this.email ?: ""
            )
        }

    }

}