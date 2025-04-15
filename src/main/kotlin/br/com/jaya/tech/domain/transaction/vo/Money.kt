package br.com.jaya.tech.domain.transaction.vo

import br.com.jaya.tech.domain.common.exception.DomainException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

data class Money(val amount: BigDecimal, val currency: CurrencyType) {

    companion object {

        private const val NON_BREAKING_SPACE_CHAR = " "

        fun of(amount: BigDecimal, currency: String, scale: Int, roundingMode: RoundingMode): Money {
            validate(amount)
            return Money(amount.setScale(scale, roundingMode), CurrencyType.findByName(currency))
        }

        private fun validate(amount: BigDecimal) {
            if (amount <= BigDecimal.ZERO) {
                throw DomainException.with("'amount' should be greater than zero")
            }
        }

    }

    fun formattedAmount(): String {
        return NumberFormat.getCurrencyInstance(currency.locale())
            .format(this.amount).replace(NON_BREAKING_SPACE_CHAR, " ")
    }

}
