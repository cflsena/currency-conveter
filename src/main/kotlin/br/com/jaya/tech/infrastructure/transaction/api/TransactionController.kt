package br.com.jaya.tech.infrastructure.transaction.api

import br.com.jaya.tech.application.transaction.create.CreateCurrencyConversionUseCase
import br.com.jaya.tech.application.transaction.list.CurrencyConversionsFilterInput
import br.com.jaya.tech.application.transaction.list.ListCurrencyConversionsUseCase
import br.com.jaya.tech.infrastructure.common.api.pagination.PageResponseDTO
import br.com.jaya.tech.infrastructure.common.config.CustomInstantDeserializer
import br.com.jaya.tech.infrastructure.transaction.mapper.TransactionMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.Instant

data class CreateCurrencyConversionRequest(
    @Schema(
        description = "User Id to create transaction",
        example = "a8ac2072-bb41-458f-8a75-aaefbd24a42b",
        required = true,
    )
    val userId: String,
    @Schema(
        description = "Origin currency code",
        example = "BRL",
        allowableValues = ["BRL", "USD", "JPY", "EUR"],
        required = true,
    )
    val originCurrency: String,
    @Schema(description = "Origin currency amount", example = "100", minimum = "1", required = true)
    val originAmount: BigDecimal,
    @Schema(
        description = "Destination currency code",
        example = "USD",
        allowableValues = ["BRL", "USD", "JPY", "EUR"],
        required = true,
    )
    val destinationCurrency: String,
)

data class CurrencyConversionResponse(
    @Schema(description = "Transaction Id created", example = "0abe1f13-bec1-4bfd-b14c-9c806ce0a846")
    val id: String,
    @Schema(description = "Transaction related user ID", example = "a8ac2072-bb41-458f-8a75-aaefbd24a42b")
    val userId: String,
    @Schema(description = "Origin currency code", example = "BRL")
    val originCurrency: String,
    @Schema(description = "Origin currency amount", example = "100")
    val originAmount: BigDecimal,
    @Schema(description = "Formatted origin currency amount", example = "R$ 100,00")
    val originAmountFormatted: String,
    @Schema(description = "Destination currency code", example = "USD")
    val destinationCurrency: String,
    @Schema(description = "Destination currency amount", example = "17.02")
    val destinationAmount: BigDecimal,
    @Schema(description = "Formatted destination currency amount", example = "$17.02")
    val destinationAmountFormatted: String,
    @Schema(description = "Used conversion rate", example = "0.1702273")
    val conversionRate: BigDecimal,
    @Schema(description = "Transaction creation date (UTC)", example = "2025-04-18T19:23:45.123Z")
    @field:JsonDeserialize(using = CustomInstantDeserializer::class)
    val createdAt: Instant,
)

@RestController
class TransactionController(
    private val createCurrencyConversionUseCase: CreateCurrencyConversionUseCase,
    private val listCurrencyConversionsUseCase: ListCurrencyConversionsUseCase,
) : TransactionApi {
    override fun createCurrencyConversion(request: CreateCurrencyConversionRequest): ResponseEntity<CurrencyConversionResponse> {
        val currencyConversionCreated = createCurrencyConversionUseCase.execute(TransactionMapper.toInput(request))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(TransactionMapper.toResponse(currencyConversionCreated))
    }

    override fun listCurrencyConversions(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
    ): ResponseEntity<PageResponseDTO<CurrencyConversionResponse>> {
        val filter = CurrencyConversionsFilterInput(userId, pageNumber, pageSize)
        val conversions = listCurrencyConversionsUseCase.execute(filter)
        return ResponseEntity.ok(TransactionMapper.toResponse(conversions))
    }
}
