package br.com.jaya.tech.domain.common.entity

fun interface Entity<T : Identifier<*>> {
    fun id(): T
}
