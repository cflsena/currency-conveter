package br.com.jaya.tech.domain.transaction

fun interface TransactionRepository {
    fun save(transaction: Transaction) : Transaction
}