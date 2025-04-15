package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.exception.DomainException

data class Email(val value: String) {
    companion object {

        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

        fun of(value: String?): Email {
            validate(value)
            return Email(value!!)
        }

        private fun validate(value: String?) {
            if (value.isNullOrBlank()) {
                throw DomainException.with("'email' should not be null or empty")
            }
            if (!EMAIL_REGEX.matches(value)) {
                throw DomainException.with("'email' is not a valid email format")
            }
        }

    }
}
