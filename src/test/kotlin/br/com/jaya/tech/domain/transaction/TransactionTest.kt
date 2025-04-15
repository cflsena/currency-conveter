package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import kotlin.test.assertNotNull

@DisplayName("Unit Test for Transaction Entity")
class TransactionTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "85fc5f36-fe55-4d77-816d-8f6edfd395d5"])
    fun givenValidParams_whenCallBuilder_shouldInstantiateTransaction(expectedId: String) {

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL.name
        val expectedDestinationCurrency = CurrencyType.USD.name
        val expectedDestinationAmount = BigDecimal("17.12")
        val expectedConversionRate = BigDecimal("0.1711596")

        val transactionCreated = Transaction.builder()
            .id(expectedId)
            .userId(expectedUserId)
            .originAmount(expectedOriginAmount)
            .originCurrency(expectedOriginCurrency)
            .destinationCurrency(expectedDestinationCurrency)
            .conversionRate(expectedConversionRate)
            .build()

        assertNotNull(transactionCreated.id())
        assertNotNull(transactionCreated.id().value())
        assertNotNull(transactionCreated.createdAt)

        if (expectedId.isNotBlank()) assertEquals(expectedId, transactionCreated.id().value())

        assertEquals(expectedUserId, transactionCreated.userId)
        assertEquals(expectedOriginAmount, transactionCreated.originMoney.amount)
        assertEquals(expectedOriginCurrency, transactionCreated.originMoney.currency.name)
        assertEquals(expectedDestinationCurrency, transactionCreated.destinationMoney.currency.name)
        assertEquals(expectedDestinationAmount, transactionCreated.destinationMoney.amount)
        assertEquals(expectedConversionRate, transactionCreated.conversionRate)

    }

    @ParameterizedTest
    @NullAndEmptySource
    fun givenAnInvalidUserId_whenCallBuilder_shouldThrowsAnException(invalidUserId: String?) {

        val expectedErrorMessage = "'userId' should not be null or empty"

        val transactionCreated = Transaction.builder()
            .originAmount(BigDecimal("100.00"))
            .originCurrency(CurrencyType.BRL.name)
            .destinationCurrency(CurrencyType.USD.name)
            .conversionRate(BigDecimal("0.1711596"))

        if (invalidUserId != null) {
            transactionCreated.userId(invalidUserId)
        }

        val exception = Assertions.assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertEquals(expectedErrorMessage, exception.message)

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["0"])
    fun givenAnInvalidConversionRate_whenCallBuilder_shouldThrowsAnException(invalidConversionRate: BigDecimal?) {

        val expectedErrorMessage01 = "'conversionRate' should not be null"
        val expectedErrorMessage02 = "'conversionRate' should be greater than zero"


        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("100.00"))
            .originCurrency(CurrencyType.BRL.name)
            .destinationCurrency(CurrencyType.USD.name)

        if (invalidConversionRate != null) {
            transactionCreated.conversionRate(invalidConversionRate)
        }

        val exception = Assertions.assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @NullSource
    @ValueSource(strings = ["0", "-1"])
    @ParameterizedTest
    fun givenAnInvalidOriginAmount_whenCallBuilder_shouldThrowsAnException(invalidOriginAmount: BigDecimal?) {

        val expectedErrorMessage01 = "'originAmount' should not be null"
        val expectedErrorMessage02 = "'amount' should be greater than zero"

        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originCurrency(CurrencyType.BRL.name)
            .destinationCurrency(CurrencyType.USD.name)
            .conversionRate(BigDecimal("0.1711596"))

        if(invalidOriginAmount != null) {
            transactionCreated.originAmount(invalidOriginAmount)
        }

        val exception = Assertions.assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = ["ABCD"])
    fun givenAnInvalidOriginCurrency_whenCallBuilder_shouldThrowsAnException(invalidOriginCurrency: String?) {

        val expectedErrorMessage01 = "'originCurrency' should not be null or empty"
        val expectedErrorMessage02 = "currency $invalidOriginCurrency invalid, available values ${CurrencyType.entries}"

        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("100.00"))
            .destinationCurrency(CurrencyType.USD.name)
            .conversionRate(BigDecimal("0.1711596"))

        if (invalidOriginCurrency != null) {
            transactionCreated.originCurrency(invalidOriginCurrency)
        }

        val exception = Assertions.assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @ParameterizedTest
    @NullAndEmptySource
    fun givenAnInvalidDestinationCurrency_whenCallBuilder_shouldThrowsAnException(invalidDestinationCurrency: String?) {

        val expectedErrorMessage = "'destinationCurrency' should not be null or empty"

        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("100.00"))
            .originCurrency(CurrencyType.BRL.name)
            .conversionRate(BigDecimal("0.1711596"))

        if (invalidDestinationCurrency != null) {
            transactionCreated.destinationCurrency(invalidDestinationCurrency)
        }

        val exception = Assertions.assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertEquals(expectedErrorMessage, exception.message)

    }

    @ParameterizedTest
    @CsvSource(
        "BRL;USD;R$ 10.000,00;$1,711.60",
        "BRL;EUR;R$ 10.000,00;1.711,60 €",
        "BRL;JPY;R$ 10.000,00;￥1,712",
        "USD;EUR;$10,000.00;1.711,60 €",
        "USD;JPY;$10,000.00;￥1,712",
        "EUR;JPY;10.000,00 €;￥1,712",
        delimiter = ';'
    )
    fun givenValidTransaction_whenGetFormattedAmount_shouldReturnOriginAndDestinationFormattedMoney(
        originCurrency: CurrencyType,
        destinationCurrency: CurrencyType,
        originFormattedMoney: String,
        destinationFormattedMoney: String
    ) {

        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("10000"))
            .originCurrency(originCurrency.name)
            .destinationCurrency(destinationCurrency.name)
            .conversionRate(BigDecimal("0.1711596"))
            .build()

        assertEquals(originFormattedMoney, transactionCreated.originMoney.formattedAmount())
        assertEquals(destinationFormattedMoney, transactionCreated.destinationMoney.formattedAmount())

    }

}
