package br.com.jaya.tech.application.transaction.list

import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import jakarta.inject.Named
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class CurrencyConversionsFilterInput(
    val userId: String,
    val pageNumber: Int,
    val pageSize: Int,
)

data class CurrencyConversionsOutput(
    val id: String,
    val userId: String,
    val originCurrency: String,
    val originAmount: BigDecimal,
    val originAmountFormatted: String,
    val destinationCurrency: String,
    val destinationAmount: BigDecimal,
    val destinationAmountFormatted: String,
    val conversionRate: BigDecimal,
    val createdAt: Instant,
) {
    companion object {
        fun from(page: PageDTO<Transaction>): PageDTO<CurrencyConversionsOutput> =
            PageDTO(
                page.pageNumber,
                page.pageSize,
                page.numberOfElements,
                page.totalPages,
                page.totalElements,
                if (page.items.isNotEmpty()) page.items.map { from(it) } else listOf(),
            )

        fun from(transaction: Transaction): CurrencyConversionsOutput =
            CurrencyConversionsOutput(
                transaction.id().value(),
                transaction.userId,
                transaction.originMoney.currency.name,
                transaction.originMoney.amount,
                transaction.originMoney.formattedAmount(),
                transaction.destinationMoney.currency.name,
                transaction.destinationMoney.amount,
                transaction.destinationMoney.formattedAmount(),
                transaction.conversionRate,
                transaction.createdAt,
            )
    }
}

@Named
class DefaultListCurrencyConversionsUseCase(
    private val transactionRepository: TransactionRepository,
) : ListCurrencyConversionsUseCase {
    override fun execute(input: CurrencyConversionsFilterInput): PageDTO<CurrencyConversionsOutput> {
        val transactionPage = transactionRepository.findAll(UUID.fromString(input.userId), input.pageNumber, input.pageSize)
        return CurrencyConversionsOutput.from(transactionPage)
    }
}
