package br.com.jaya.tech.domain.transaction.vo

import br.com.jaya.tech.domain.common.entity.ValueObject
import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.shared.assert.AssertThrows
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

data class Money(val amount: BigDecimal, val currency: CurrencyType) : ValueObject() {

    companion object {

        private const val NON_BREAKING_SPACE_CHAR = " "

        fun of(amount: BigDecimal, currency: String, scale: Int, roundingMode: RoundingMode): Money {
            AssertThrows.isTrue(amount > BigDecimal.ZERO)
            { DomainException.with("'amount' should be greater than zero") }
            return Money(amount.setScale(scale, roundingMode), CurrencyType.findByName(currency))
        }

    }

    fun formattedAmount(): String {
        return NumberFormat.getCurrencyInstance(currency.locale())
            .format(this.amount).replace(NON_BREAKING_SPACE_CHAR, " ")
    }

}
