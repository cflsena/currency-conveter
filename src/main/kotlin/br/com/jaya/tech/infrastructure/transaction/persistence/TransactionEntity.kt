package br.com.jaya.tech.infrastructure.transaction.persistence

import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import br.com.jaya.tech.infrastructure.user.persistence.UserEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "tb_transaction")
class TransactionEntity(

    @Id
    @field:NotNull
    @field:NotBlank
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "origin_currency", nullable = false, updatable = false)
    val originCurrency: CurrencyType,

    @field:NotNull
    @Column(name = "origin_amount", nullable = false, updatable = false)
    val originAmount: BigDecimal,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "destination_currency", nullable = false, updatable = false)
    val destinationCurrency: CurrencyType,

    @field:NotNull
    @Column(name = "conversion_rate", nullable = false, updatable = false)
    val conversionRate: BigDecimal,

    @field:NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

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
        private var createdAt: Instant? = null

        fun id(id: String) = apply { this.id = id }
        fun userId(userId: String) = apply { this.userId = userId }
        fun originCurrency(originCurrency: String) = apply { this.originCurrency = originCurrency }
        fun originAmount(originAmount: BigDecimal) = apply { this.originAmount = originAmount }
        fun destinationCurrency(destinationCurrency: String) = apply { this.destinationCurrency = destinationCurrency }
        fun conversionRate(conversionRate: BigDecimal) = apply { this.conversionRate = conversionRate }
        fun createdAt(createdAt: Instant) = apply { this.createdAt = createdAt }

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