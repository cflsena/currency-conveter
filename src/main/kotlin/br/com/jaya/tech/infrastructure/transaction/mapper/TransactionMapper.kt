package br.com.jaya.tech.infrastructure.transaction.mapper

import br.com.jaya.tech.application.transaction.create.CreateCurrencyConversionInput
import br.com.jaya.tech.application.transaction.create.CreateCurrencyConversionOutput
import br.com.jaya.tech.application.transaction.list.CurrencyConversionsOutput
import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.infrastructure.common.api.pagination.PageResponseDTO
import br.com.jaya.tech.infrastructure.transaction.api.CreateCurrencyConversionRequest
import br.com.jaya.tech.infrastructure.transaction.api.CurrencyConversionResponse
import br.com.jaya.tech.infrastructure.transaction.persistence.TransactionEntity
import org.springframework.data.domain.Page
import java.util.*

object TransactionMapper {
    fun toEntity(transaction: Transaction): TransactionEntity =
        TransactionEntity
            .builder()
            .id(UUID.fromString(transaction.id().value()))
            .userId(UUID.fromString(transaction.userId))
            .originCurrency(transaction.originMoney.currency.name)
            .originAmount(transaction.originMoney.amount)
            .destinationCurrency(transaction.destinationMoney.currency.name)
            .conversionRate(transaction.conversionRate)
            .createdAt(transaction.createdAt)
            .build()

    fun toDomain(transaction: TransactionEntity): Transaction =
        Transaction
            .builder()
            .id(transaction.id.toString())
            .userId(transaction.user.id.toString())
            .originCurrency(transaction.originCurrency)
            .originAmount(transaction.originAmount)
            .destinationCurrency(transaction.destinationCurrency)
            .conversionRate(transaction.conversionRate)
            .createdAt(transaction.createdAt)
            .build()

    fun toPage(page: Page<TransactionEntity>): PageDTO<Transaction> =
        PageDTO(
            page.number,
            page.size,
            page.numberOfElements,
            page.totalPages,
            page.totalElements.toInt(),
            page.content.map { toDomain(it) },
        )

    fun toInput(request: CreateCurrencyConversionRequest): CreateCurrencyConversionInput =
        CreateCurrencyConversionInput(
            request.userId,
            request.originAmount,
            request.originCurrency,
            request.destinationCurrency,
        )

    fun toResponse(output: CreateCurrencyConversionOutput): CurrencyConversionResponse =
        CurrencyConversionResponse(
            output.id,
            output.userId,
            output.originCurrency,
            output.originAmount,
            output.originAmountFormatted,
            output.destinationCurrency,
            output.destinationAmount,
            output.destinationAmountFormatted,
            output.conversionRate,
            output.createdAt,
        )

    fun toResponse(page: PageDTO<CurrencyConversionsOutput>): PageResponseDTO<CurrencyConversionResponse> =
        PageResponseDTO(
            page.pageNumber,
            page.pageSize,
            page.numberOfElements,
            page.totalPages,
            page.totalElements,
            page.items.map { toResponse(it) },
        )

    private fun toResponse(output: CurrencyConversionsOutput): CurrencyConversionResponse =
        CurrencyConversionResponse(
            output.id,
            output.userId,
            output.originCurrency,
            output.originAmount,
            output.originAmountFormatted,
            output.destinationCurrency,
            output.destinationAmount,
            output.destinationAmountFormatted,
            output.conversionRate,
            output.createdAt,
        )
}
