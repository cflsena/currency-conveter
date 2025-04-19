package br.com.jaya.tech.application.transaction.create

import br.com.jaya.tech.application.service.ConversionRateService
import br.com.jaya.tech.domain.common.exception.BusinessException
import br.com.jaya.tech.domain.common.exception.NotFoundException
import br.com.jaya.tech.domain.transaction.ConversionRateCalculator
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import br.com.jaya.tech.domain.user.UserRepository
import br.com.jaya.tech.shared.assert.AssertThrows
import jakarta.inject.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Instant

data class CreateCurrencyConversionInput(
    val userId: String,
    val originAmount: BigDecimal,
    val originCurrency: String,
    val destinationCurrency: String,
)

data class CreateCurrencyConversionOutput(
    val id: String,
    val userId: String,
    val originCurrency: String,
    val originAmount: BigDecimal,
    val originAmountFormatted: String,
    val destinationCurrency: String,
    val destinationAmount: BigDecimal,
    val destinationAmountFormatted: String,
    val conversionRate: BigDecimal,
    val createdAt: Instant,
) {
    companion object {
        fun of(transaction: Transaction): CreateCurrencyConversionOutput =
            CreateCurrencyConversionOutput(
                transaction.id().value(),
                transaction.userId,
                transaction.originMoney.currency.name,
                transaction.originMoney.amount,
                transaction.originMoney.formattedAmount(),
                transaction.destinationMoney.currency.name,
                transaction.destinationMoney.amount,
                transaction.destinationMoney.formattedAmount(),
                transaction.conversionRate,
                transaction.createdAt,
            )
    }
}

@Named
class DefaultCreateCurrencyConversionUseCase(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val conversionRateService: ConversionRateService,
) : CreateCurrencyConversionUseCase {
    private val log: Logger = LoggerFactory.getLogger(CreateCurrencyConversionUseCase::class.java)

    override fun execute(input: CreateCurrencyConversionInput): CreateCurrencyConversionOutput {
        log.info(
            "Performing conversion from origin {} {} to destination {}",
            input.originCurrency,
            input.originAmount,
            input.destinationCurrency,
        )

        AssertThrows.isTrue(userRepository.existsById(input.userId)) {
            throw NotFoundException.with("Unable to perform conversion, user with id ${input.userId} not found")
        }

        val originCurrency = CurrencyType.findByName(input.originCurrency)
        val destinationCurrency = CurrencyType.findByName(input.destinationCurrency)
        val conversionRate = retrieveConversionRate(originCurrency, destinationCurrency)

        val transactionToSave =
            Transaction
                .builder()
                .userId(input.userId)
                .originAmount(input.originAmount)
                .originCurrency(originCurrency)
                .destinationCurrency(destinationCurrency)
                .conversionRate(conversionRate)
                .build()

        val transactionCreated = transactionRepository.save(transactionToSave)

        log.info("Transaction conversion {} performed successfully", transactionCreated)

        return CreateCurrencyConversionOutput.of(transactionCreated)
    }

    private fun retrieveConversionRate(
        originCurrency: CurrencyType,
        destinationCurrency: CurrencyType,
    ): BigDecimal {
        AssertThrows.isFalse(originCurrency == destinationCurrency) {
            BusinessException.with("Unable to perform conversion, please inform two different currencies")
        }

        val originRate =
            conversionRateService.getRates()[originCurrency.name]
                ?: throw BusinessException.with("Origin currency not available for conversion. Please select another one")

        val destinationRate =
            conversionRateService.getRates()[destinationCurrency.name]
                ?: throw BusinessException.with("Destination currency not available for conversion. Please select another one")

        return ConversionRateCalculator.calculate(originRate, destinationRate)
    }
}
