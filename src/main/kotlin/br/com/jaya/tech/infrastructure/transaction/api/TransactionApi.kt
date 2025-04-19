package br.com.jaya.tech.infrastructure.transaction.api

import br.com.jaya.tech.infrastructure.common.api.ApiBaseDocumentation
import br.com.jaya.tech.infrastructure.common.api.pagination.PageResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/jaya-tech/transactions")
@Tag(name = "Transaction", description = "API to manage currency conversions transactions")
interface TransactionApi : ApiBaseDocumentation {
    @Operation(summary = "Endpoint to convert two currencies", description = "Conversion are based on EUR currency")
    @ApiResponse(responseCode = "201", description = "Created successfully")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createCurrencyConversion(
        @RequestBody request: CreateCurrencyConversionRequest,
    ): ResponseEntity<CurrencyConversionResponse>

    @Operation(summary = "Endpoint to list a user's currency conversion transactions")
    @ApiResponse(responseCode = "200", description = "Listed successfully")
    @GetMapping("/filter")
    fun listCurrencyConversions(
        @Parameter(name = "userId", description = "User id to list transactions")
        @RequestParam("userId") userId: String,
        @Parameter(name = "pageNumber", description = "Page index (pages start at position 0)")
        @RequestParam("pageNumber") pageNumber: Int,
        @Parameter(name = "pageSize", description = "Number of elements expected on the page")
        @RequestParam("pageSize") pageSize: Int,
    ): ResponseEntity<PageResponseDTO<CurrencyConversionResponse>>
}
