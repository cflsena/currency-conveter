package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.pagination.PageDTO

interface TransactionRepository {
    fun save(transaction: Transaction) : Transaction
    fun findAll(userId: String, pageNumber: Int, pageSize: Int): PageDTO<Transaction>
}