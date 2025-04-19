package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.pagination.PageDTO
import java.util.*

interface TransactionRepository {
    fun save(transaction: Transaction): Transaction

    fun findAll(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
    ): PageDTO<Transaction>
}
