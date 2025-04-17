package br.com.jaya.tech.infrastructure.transaction.persistence

import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import br.com.jaya.tech.infrastructure.user.persistence.UserEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "tb_transaction")
class TransactionEntity(

    @Id
    @NotNull
    @NotBlank
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: String,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "origin_currency", nullable = false, updatable = false)
    val originCurrency: CurrencyType,

    @NotNull
    @Column(name = "origin_amount", nullable = false, updatable = false)
    val originAmount: BigDecimal,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "destination_currency", nullable = false, updatable = false)
    val destinationCurrency: CurrencyType,

    @NotNull
    @Column(name = "conversion_rate", nullable = false, updatable = false)
    val conversionRate: BigDecimal,

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    val user: UserEntity,

    ) {

    companion object {
        fun builder(): Builder {
            return Builder()
        }

    }

    class Builder {

        private var id: String? = null
        private var userId: String? = null
        private var originCurrency: String? = null
        private var originAmount: BigDecimal? = null
        private var destinationCurrency: String? = null
        private var conversionRate: BigDecimal? = null
        private var createdAt: LocalDateTime? = null

        fun id(id: String) = apply { this.id = id }
        fun userId(userId: String) = apply { this.userId = userId }
        fun originCurrency(originCurrency: String) = apply { this.originCurrency = originCurrency }
        fun originAmount(originAmount: BigDecimal) = apply { this.originAmount = originAmount }
        fun destinationCurrency(destinationCurrency: String) = apply { this.destinationCurrency = destinationCurrency }
        fun conversionRate(conversionRate: BigDecimal) = apply { this.conversionRate = conversionRate }
        fun createdAt(createdAt: LocalDateTime) = apply { this.createdAt = createdAt }

        fun build(): TransactionEntity {
            return TransactionEntity(
                this.id!!,
                CurrencyType.findByName(this.originCurrency!!),
                this.originAmount!!,
                CurrencyType.findByName(this.destinationCurrency!!),
                this.conversionRate!!,
                this.createdAt!!,
                UserEntity.builder().id(this.userId!!).build()
            )
        }

    }

}