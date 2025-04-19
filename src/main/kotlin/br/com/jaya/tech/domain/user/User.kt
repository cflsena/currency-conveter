package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.entity.Entity
import br.com.jaya.tech.domain.common.entity.Identifier
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.user.vo.Email
import br.com.jaya.tech.domain.user.vo.Name

data class UserId(
    private val value: String,
) : Identifier<String> {
    companion object {
        fun create(): UserId = UserId(IdUtils.generate())

        fun create(id: String): UserId = UserId(id)
    }

    override fun value(): String = this.value
}

class User private constructor(
    private val id: UserId,
    name: Name,
    email: Email,
) : Entity<UserId> {
    var name: Name = name
        private set

    var email: Email = email
        private set

    fun updateEmail(email: String) {
        this.email = Email.of(email)
    }

    fun getFormattedName(): String = "${this.name.givenName} ${this.name.familyName}"

    companion object {
        fun builder(): Builder = Builder()
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
            val id = if (this.id == null) UserId.create() else UserId.create(this.id!!)
            val name = Name.of(this.givenName, this.familyName)
            val email = Email.of(this.email)
            return User(id, name, email)
        }
    }

    override fun id(): UserId = this.id

    override fun toString(): String = "User(id=$id, name=$name, email=$email)"
}
