package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.shared.assert.AssertThrows
import java.math.BigDecimal

object TransactionValidator {

    fun validate(
        userId: String?,
        conversionRate: BigDecimal?,
        originAmount: BigDecimal?,
        originCurrency: String?,
        destinationCurrency: String?
    ) {

        AssertThrows.isTrue(!userId.isNullOrBlank()) { DomainException.with("'userId' should not be null or empty") }

        AssertThrows.isNotNull(conversionRate) { DomainException.with("'conversionRate' should not be null") }
        AssertThrows.isTrue(conversionRate!! > BigDecimal.ZERO)
        { DomainException.with("'conversionRate' should be greater than zero") }

        AssertThrows.isNotNull(originAmount) { DomainException.with("'originAmount' should not be null") }
        AssertThrows.isTrue(originAmount!! > BigDecimal.ZERO)
        { DomainException.with("'originAmount' should not be null") }

        AssertThrows.isTrue(!originCurrency.isNullOrBlank())
        { DomainException.with("'originCurrency' should not be null or empty") }

        AssertThrows.isTrue(!destinationCurrency.isNullOrBlank())
        { DomainException.with("'destinationCurrency' should not be null or empty") }

    }

}
