package br.com.jaya.tech.infrastructure.transaction.persistence

import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import br.com.jaya.tech.infrastructure.transaction.mapper.TransactionMapper
import br.com.jaya.tech.infrastructure.transaction.persistence.jpa.TransactionJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class DefaultTransactionRepository(
    private val repository: TransactionJpaRepository
) : TransactionRepository {

    override fun save(transaction: Transaction): Transaction =
        repository.save(TransactionMapper.toEntity(transaction)).let(TransactionMapper::toDomain)

    override fun findAll(userId: String, pageNumber: Int, pageSize: Int): PageDTO<Transaction> {
        val transactionPage = repository.findAllByUserId(userId, PageRequest.of(pageNumber, pageSize))
        return TransactionMapper.toPage(transactionPage)
    }

}