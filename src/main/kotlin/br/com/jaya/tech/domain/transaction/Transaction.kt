package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.entity.Entity
import br.com.jaya.tech.domain.common.entity.Identifier
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import br.com.jaya.tech.domain.transaction.vo.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant

data class TransactionId(
    private val value: String,
) : Identifier<String> {
    companion object {
        fun create(): TransactionId = TransactionId(IdUtils.generate())

        fun create(id: String): TransactionId = TransactionId(id)
    }

    override fun value(): String = this.value
}

class Transaction private constructor(
    private val id: TransactionId,
    val userId: String,
    val originMoney: Money,
    val destinationMoney: Money,
    val conversionRate: BigDecimal,
    val createdAt: Instant,
) : Entity<TransactionId> {
    companion object {
        fun builder(): Builder = Builder()

        private const val DEFAULT_AMOUNT_SCALE_SIZE = 2
        private val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN
    }

    class Builder {
        private var id: String? = null
        private var userId: String? = null
        private var originAmount: BigDecimal? = null
        private var originCurrency: CurrencyType? = null
        private var destinationCurrency: CurrencyType? = null
        private var conversionRate: BigDecimal? = null
        private var createdAt: Instant? = null

        fun id(id: String) = apply { this.id = id }

        fun userId(userId: String) = apply { this.userId = userId }

        fun originAmount(originAmount: BigDecimal) = apply { this.originAmount = originAmount }

        fun originCurrency(originCurrency: CurrencyType) = apply { this.originCurrency = originCurrency }

        fun destinationCurrency(destinationCurrency: CurrencyType) = apply { this.destinationCurrency = destinationCurrency }

        fun conversionRate(conversionRate: BigDecimal) = apply { this.conversionRate = conversionRate }

        fun createdAt(createdAt: Instant) = apply { this.createdAt = createdAt }

        fun build(): Transaction {
            TransactionValidator.validate(
                this.userId,
                this.conversionRate,
                this.originAmount,
                this.originCurrency,
                this.destinationCurrency,
            )

            val id = if (this.id == null) TransactionId.create() else TransactionId.create(this.id!!)
            val userId = this.userId!!

            val createdAt = this.createdAt ?: Instant.now()

            val originMoney =
                Money.of(
                    this.originAmount!!,
                    this.originCurrency!!,
                    DEFAULT_AMOUNT_SCALE_SIZE,
                    DEFAULT_ROUNDING_MODE,
                )

            val destinationMoney =
                Money.of(
                    originMoney.amount.multiply(this.conversionRate),
                    this.destinationCurrency!!,
                    DEFAULT_AMOUNT_SCALE_SIZE,
                    DEFAULT_ROUNDING_MODE,
                )

            return Transaction(id, userId, originMoney, destinationMoney, this.conversionRate!!, createdAt)
        }
    }

    override fun id(): TransactionId = this.id

    override fun toString(): String =
        "Transaction(id=$id, userId='$userId', originMoney=$originMoney, destinationMoney=$destinationMoney, " +
            "conversionRate=$conversionRate, createdAt=$createdAt)"
}
