package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.entity.Entity
import br.com.jaya.tech.domain.common.entity.Identifier
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.user.vo.Email
import br.com.jaya.tech.domain.user.vo.Name

data class UserId(private val value: String) : Identifier<String> {

    companion object {
        fun create(): UserId {
            return UserId(IdUtils.generate())
        }

        fun create(id: String): UserId {
            return UserId(id)
        }
    }

    override fun value(): String {
        return this.value
    }

}

class User private constructor(private val id: UserId, name: Name, email: Email) : Entity<UserId> {

    var name: Name = name
        private set

    var email: Email = email
        private set

    fun updateEmail(email: String) {
        this.email = Email.of(email)
    }

    fun updateName(givenName: String, familyName: String) {
        this.name = Name.of(givenName, familyName)
    }

    fun getFormattedName(): String {
        return "${this.name.givenName} ${this.name.familyName}"
    }

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

    override fun id(): UserId {
        return this.id
    }

    override fun toString(): String {
        return "User(id=$id, name=$name, email=$email)"
    }

}
