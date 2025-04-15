package br.com.jaya.tech.domain.common

fun interface Identifier<T> {
    fun value() : T
}