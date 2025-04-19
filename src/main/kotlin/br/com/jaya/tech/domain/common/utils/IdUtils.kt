package br.com.jaya.tech.domain.common.utils

import java.util.*

object IdUtils {
    fun generate(): String = UUID.randomUUID().toString()
}
