package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.vo.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

data class TransactionId(val value: String) {
    companion object {
        fun create(): TransactionId {
            return TransactionId(IdUtils.generate())
        }

        fun create(id: String): TransactionId {
            return TransactionId(id)
        }
    }
}

class Transaction private constructor(
    val id: TransactionId,
    val userId: String,
    val originMoney: Money,
    val destinationMoney: Money,
    val conversionRate: BigDecimal,
    val createdAt: LocalDateTime
) {

    companion object {
        fun builder(): Builder {
            return Builder()
        }

        private const val DEFAULT_AMOUNT_SCALE_SIZE = 2
        private val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN
    }

    class Builder {

        private var id: String? = null
        private var userId: String? = null
        private var originAmount: BigDecimal? = null
        private var originCurrency: String? = null
        private var destinationCurrency: String? = null
        private var conversionRate: BigDecimal? = null

        fun id(id: String) = apply { this.id = id }
        fun userId(userId: String) = apply { this.userId = userId }
        fun originAmount(originAmount: BigDecimal) = apply { this.originAmount = originAmount }
        fun originCurrency(originCurrency: String) = apply { this.originCurrency = originCurrency }
        fun destinationCurrency(destinationCurrency: String) = apply { this.destinationCurrency = destinationCurrency }
        fun conversionRate(conversionRate: BigDecimal) = apply { this.conversionRate = conversionRate }

        fun build(): Transaction {

            TransactionValidator.validate(
                this.userId,
                this.conversionRate,
                this.originAmount,
                this.originCurrency,
                this.destinationCurrency
            )

            val id = if (this.id == null) TransactionId.create() else TransactionId.create(this.id!!)
            val userId = this.userId!!
            val conversionRateWithScale = this.conversionRate!!
            val createdAt = LocalDateTime.now()

            val originMoney = Money.of(
                this.originAmount!!,
                this.originCurrency!!,
                DEFAULT_AMOUNT_SCALE_SIZE,
                DEFAULT_ROUNDING_MODE
            )

            val destinationMoney = Money.of(
                originMoney.amount.multiply(conversionRateWithScale),
                this.destinationCurrency!!,
                DEFAULT_AMOUNT_SCALE_SIZE,
                DEFAULT_ROUNDING_MODE
            )

            return Transaction(id, userId, originMoney, destinationMoney, conversionRateWithScale, createdAt)

        }

    }

}