package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.exception.DomainException
import java.math.BigDecimal

object TransactionValidator {

    fun validate(
        userId: String?,
        conversionRate: BigDecimal?,
        originAmount: BigDecimal?,
        originCurrency: String?,
        destinationCurrency: String?
    ) {

        when {
            userId.isNullOrBlank() -> {
                throw DomainException.with("'userId' should not be null or empty")
            }
            conversionRate == null -> {
                throw DomainException.with("'conversionRate' should not be null")
            }
            conversionRate <= BigDecimal.ZERO -> {
                throw DomainException.with("'conversionRate' should be greater than zero")
            }
            originAmount == null -> {
                throw DomainException.with("'originAmount' should not be null")
            }
            originCurrency.isNullOrBlank() -> {
                throw DomainException.with("'originCurrency' should not be null or empty")
            }
            destinationCurrency.isNullOrBlank() -> {
                throw DomainException.with("'destinationCurrency' should not be null or empty")
            }
        }

    }

}