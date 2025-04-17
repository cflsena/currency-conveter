package br.com.jaya.tech.infrastructure.transaction.mapper

import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.infrastructure.transaction.persistence.TransactionEntity
import org.springframework.data.domain.Page

object TransactionMapper {

    fun toEntity(transaction: Transaction): TransactionEntity {
        return TransactionEntity.builder()
            .id(transaction.id().value())
            .userId(transaction.userId)
            .originCurrency(transaction.originMoney.currency.name)
            .originAmount(transaction.originMoney.amount)
            .destinationCurrency(transaction.destinationMoney.currency.name)
            .conversionRate(transaction.conversionRate)
            .createdAt(transaction.createdAt)
            .build()
    }

    fun toDomain(transaction: TransactionEntity): Transaction {
        return Transaction.builder()
            .id(transaction.id)
            .userId(transaction.user.id)
            .originCurrency(transaction.originCurrency.name)
            .originAmount(transaction.originAmount)
            .destinationCurrency(transaction.destinationCurrency.name)
            .conversionRate(transaction.conversionRate)
            .createdAt(transaction.createdAt)
            .build()
    }

    fun toPage(page: Page<TransactionEntity>): PageDTO<Transaction> {
        return PageDTO(
            page.number,
            page.size,
            page.numberOfElements,
            page.totalPages,
            page.totalElements.toInt(),
            page.content.map { toDomain(it) }
        )
    }

}