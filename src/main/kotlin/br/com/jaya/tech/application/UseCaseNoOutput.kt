package br.com.jaya.tech.application

fun interface UseCaseNoOutput<I> {
    fun execute(input: I)
}