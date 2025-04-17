package br.com.jaya.tech.domain.user.vo

import br.com.jaya.tech.domain.common.entity.ValueObject
import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.shared.assert.AssertThrows

data class Name(val givenName: String, val familyName: String) : ValueObject() {
    companion object {

        private const val MIN_SIZE_NAME = 3

        fun of(givenName: String?, familyName: String?): Name {
            validate(givenName, familyName)
            return Name(givenName!!, familyName!!)
        }

        private fun validate(givenName: String?, familyName: String?) {

            AssertThrows.isTrue(!givenName.isNullOrBlank())
            { DomainException.with("'givenName' should not be null or empty") }

            AssertThrows.isTrue(givenName!!.length >= MIN_SIZE_NAME)
            { DomainException.with("'givenName' invalid size, min size is $MIN_SIZE_NAME") }

            AssertThrows.isTrue(!familyName.isNullOrBlank())
            { DomainException.with("'familyName' should not be null or empty") }

            AssertThrows.isTrue(familyName!!.length >= MIN_SIZE_NAME)
            { DomainException.with("'familyName' invalid size, min size is $MIN_SIZE_NAME") }

        }

    }

}
