package br.com.jaya.tech.domain.common

fun interface Entity <T : Identifier<*>> {
    fun id(): T
}