package br.com.jaya.tech.domain.common.entity

fun interface Identifier<T> {
    fun value(): T
}
