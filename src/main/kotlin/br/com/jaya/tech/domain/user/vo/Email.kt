package br.com.jaya.tech.domain.user.vo

import br.com.jaya.tech.domain.common.entity.ValueObject
import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.shared.assert.AssertThrows

data class Email(
    val value: String,
) : ValueObject() {
    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        private const val MAX_SIZE_EMAIL = 50

        fun of(value: String?): Email {
            validate(value)
            return Email(value!!)
        }

        private fun validate(value: String?) {
            AssertThrows.isTrue(!value.isNullOrBlank()) { DomainException.with("'email' should not be null or empty") }
            AssertThrows.isTrue(
                value!!.length <= MAX_SIZE_EMAIL,
            ) { DomainException.with("'email' invalid size, max size is $MAX_SIZE_EMAIL") }
            AssertThrows.isTrue(EMAIL_REGEX.matches(value)) { DomainException.with("'email' is not a valid email format") }
        }
    }
}
