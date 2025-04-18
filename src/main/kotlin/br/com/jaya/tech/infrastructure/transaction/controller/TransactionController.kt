package br.com.jaya.tech.infrastructure.transaction.controller

import br.com.jaya.tech.application.transaction.create.CreateCurrencyConversionUseCase
import br.com.jaya.tech.application.transaction.list.CurrencyConversionsFilterInput
import br.com.jaya.tech.application.transaction.list.ListCurrencyConversionsUseCase
import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.infrastructure.transaction.mapper.TransactionMapper
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateCurrencyConversionRequest(
    val userId: String,
    val originCurrency: String,
    val originAmount: BigDecimal,
    val destinationCurrency: String
)

data class CurrencyConversionResponse(
    val id: String,
    val userId: String,
    val originCurrency: String,
    val originAmount: BigDecimal,
    val originAmountFormatted: String,
    val destinationCurrency: String,
    val destinationAmount: BigDecimal,
    val destinationAmountFormatted: String,
    val conversionRate: BigDecimal,
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
)

@RestController
class TransactionController(
    private val createCurrencyConversionUseCase: CreateCurrencyConversionUseCase,
    private val listCurrencyConversionsUseCase : ListCurrencyConversionsUseCase
) : TransactionApi {

    override fun createCurrencyConversion(request: CreateCurrencyConversionRequest):
            ResponseEntity<CurrencyConversionResponse> {
        val currencyConversionCreated = createCurrencyConversionUseCase.execute(TransactionMapper.toInput(request))
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(TransactionMapper.toResponse(currencyConversionCreated))
    }

    override fun listCurrencyConversions(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): ResponseEntity<PageDTO<CurrencyConversionResponse>> {
        val filter = CurrencyConversionsFilterInput(userId, pageNumber, pageSize)
        val conversions = listCurrencyConversionsUseCase.execute(filter)
        return ResponseEntity.ok(TransactionMapper.toResponse(conversions))
    }


}