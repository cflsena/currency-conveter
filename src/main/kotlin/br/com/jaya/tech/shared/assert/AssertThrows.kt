package br.com.jaya.tech.shared.assert

import br.com.jaya.tech.shared.exception.BaseException

object AssertThrows {

    fun isTrue(value: Boolean, exception: () -> BaseException) {
        if (!value) {
            throw exception()
        }
    }

    fun isNotNull(value: Any?, exception: () -> BaseException) {
        if (value == null) {
            throw exception()
        }
    }

}