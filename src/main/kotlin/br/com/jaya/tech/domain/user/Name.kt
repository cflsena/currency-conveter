package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.exception.DomainException

data class Name(val givenName: String, val familyName: String) {
    companion object {

        private const val MIN_SIZE_NAME = 3

        fun of(givenName: String?, familyName: String?): Name {
            validate(givenName, familyName)
            return Name(givenName!!, familyName!!)
        }

        private fun validate(givenName: String?, familyName: String?) {
            if (givenName.isNullOrBlank()) {
                throw DomainException.with("'givenName' should not be null or empty")
            }
            if (givenName.length < MIN_SIZE_NAME) {
                throw DomainException.with("'givenName' invalid size, min size is $MIN_SIZE_NAME")
            }
            if (familyName.isNullOrBlank()) {
                throw DomainException.with("'familyName' should not be null or empty")
            }
            if (familyName.length < MIN_SIZE_NAME) {
                throw DomainException.with("'familyName' invalid size, min size is $MIN_SIZE_NAME")
            }
        }

    }

}
