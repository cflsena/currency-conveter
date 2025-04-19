package br.com.jaya.tech.shared.exception

import java.io.Serializable

data class Error(
    val message: String,
) : Serializable {
    companion object {
        fun with(message: String): Error = Error(message)
    }
}

open class BaseException(
    private val error: Error,
) : RuntimeException(error.message, null, true, false)
