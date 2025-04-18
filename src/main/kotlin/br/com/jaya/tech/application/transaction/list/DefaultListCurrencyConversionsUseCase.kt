package br.com.jaya.tech.application.transaction.list

import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import jakarta.inject.Named
import java.math.BigDecimal
import java.time.LocalDateTime

data class CurrencyConversionsFilterInput(val userId: String, val pageNumber: Int, val pageSize: Int)

data class CurrencyConversionsOutput(
    val id: String,
    val userId: String,
    val originCurrency: String,
    val originAmount: BigDecimal,
    val destinationCurrency: String,
    val destinationAmount: BigDecimal,
    val conversionRate: BigDecimal,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(page: PageDTO<Transaction>): PageDTO<CurrencyConversionsOutput> {
            return PageDTO(
                page.pageNumber,
                page.pageSize,
                page.numberOfElements,
                page.totalElements,
                page.totalPages,
                if(page.items.isNotEmpty()) page.items.map { from(it) } else listOf()
            )
        }

        fun from(transaction: Transaction): CurrencyConversionsOutput {
            return CurrencyConversionsOutput(
                transaction.id().value(),
                transaction.userId,
                transaction.originMoney.currency.name,
                transaction.originMoney.amount,
                transaction.destinationMoney.currency.name,
                transaction.destinationMoney.amount,
                transaction.conversionRate,
                transaction.createdAt,
            )
        }

    }
}

@Named
class DefaultListCurrencyConversionsUseCase(private val transactionRepository: TransactionRepository) : ListCurrencyConversionsUseCase {

    override fun execute(input: CurrencyConversionsFilterInput): PageDTO<CurrencyConversionsOutput> {
        val transactionPage = transactionRepository.findAll(input.userId, input.pageNumber, input.pageSize)
        return CurrencyConversionsOutput.from(transactionPage)
    }

}
