package br.com.jaya.tech.infrastructure.transaction.controller

import br.com.jaya.tech.domain.common.pagination.PageDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/jaya-tech/transactions")
interface TransactionApi {

    @PostMapping
    fun createCurrencyConversion(@RequestBody request: CreateCurrencyConversionRequest):
            ResponseEntity<CurrencyConversionResponse>

    @GetMapping("/filter")
    fun listCurrencyConversions(
        @RequestParam("userId") userId: String,
        @RequestParam("pageNumber") pageNumber: Int,
        @RequestParam("pageSize") pageSize: Int
    ): ResponseEntity<PageDTO<CurrencyConversionResponse>>

}