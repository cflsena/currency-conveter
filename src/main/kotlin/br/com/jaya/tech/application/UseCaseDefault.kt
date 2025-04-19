package br.com.jaya.tech.application

fun interface UseCaseDefault<I, O> {
    fun execute(input: I): O
}
