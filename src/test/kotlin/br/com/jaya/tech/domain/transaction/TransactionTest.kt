package br.com.jaya.tech.domain.transaction

import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

@DisplayName("Unit Test for Transaction Entity")
class TransactionTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "85fc5f36-fe55-4d77-816d-8f6edfd395d5"])
    fun givenValidParams_whenCallBuilder_shouldInstantiateTransaction(expectedId: String) {

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL
        val expectedDestinationCurrency = CurrencyType.USD
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
        assertEquals(expectedOriginCurrency, transactionCreated.originMoney.currency)
        assertEquals(expectedDestinationCurrency, transactionCreated.destinationMoney.currency)
        assertEquals(expectedDestinationAmount, transactionCreated.destinationMoney.amount)
        assertEquals(expectedConversionRate, transactionCreated.conversionRate)

    }

    @ParameterizedTest
    @NullAndEmptySource
    fun givenAnInvalidUserId_whenCallBuilder_shouldThrowsAnException(invalidUserId: String?) {

        val expectedErrorMessage = "'userId' should not be null or empty"

        val transactionCreated = Transaction.builder()
            .originAmount(BigDecimal("100.00"))
            .originCurrency(CurrencyType.BRL)
            .destinationCurrency(CurrencyType.USD)
            .conversionRate(BigDecimal("0.1711596"))

        if (invalidUserId != null) {
            transactionCreated.userId(invalidUserId)
        }

        val exception = assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertEquals(expectedErrorMessage, exception.message)

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["0", "-1"])
    fun givenAnInvalidConversionRate_whenCallBuilder_shouldThrowsAnException(invalidConversionRate: BigDecimal?) {

        val expectedErrorMessage01 = "'conversionRate' should not be null"
        val expectedErrorMessage02 = "'conversionRate' should be greater than zero"


        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("100.00"))
            .originCurrency(CurrencyType.BRL)
            .destinationCurrency(CurrencyType.USD)

        if (invalidConversionRate != null) {
            transactionCreated.conversionRate(invalidConversionRate)
        }

        val exception = assertThrows(
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
            .originCurrency(CurrencyType.BRL)
            .destinationCurrency(CurrencyType.USD)
            .conversionRate(BigDecimal("0.1711596"))

        if(invalidOriginAmount != null) {
            transactionCreated.originAmount(invalidOriginAmount)
        }

        val exception = assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["ABCD"])
    fun givenAnInvalidOriginCurrency_whenCallBuilder_shouldThrowsAnException(invalidOriginCurrency: String?) {

        val expectedErrorMessage01 = "'originCurrency' should not be null or empty"
        val expectedErrorMessage02 = "currency $invalidOriginCurrency invalid, available values ${CurrencyType.entries}"

        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("100.00"))
            .destinationCurrency(CurrencyType.USD)
            .conversionRate(BigDecimal("0.1711596"))

        val exception = assertThrows(
            DomainException::class.java,
        ) { transactionCreated.build() }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @Test
    fun givenAnInvalidDestinationCurrency_whenCallBuilder_shouldThrowsAnException() {

        val expectedErrorMessage = "'destinationCurrency' should not be null or empty"

        val transactionCreated = Transaction.builder()
            .userId(IdUtils.generate())
            .originAmount(BigDecimal("100.00"))
            .originCurrency(CurrencyType.BRL)
            .conversionRate(BigDecimal("0.1711596"))

        val exception = assertThrows(
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
            .originCurrency(originCurrency)
            .destinationCurrency(destinationCurrency)
            .conversionRate(BigDecimal("0.1711596"))
            .build()

        assertEquals(originFormattedMoney, transactionCreated.originMoney.formattedAmount())
        assertEquals(destinationFormattedMoney, transactionCreated.destinationMoney.formattedAmount())

    }

    @Test
    fun givenTwoTransactionIds_whenCallCreate_shouldHaveTwoUniqueAndNonEmptyIds() {
        val transactionId01 = TransactionId.create()
        val transactionId2 = TransactionId.create()
        assertNotNull(transactionId01.value())
        assertTrue(transactionId01.value().isNotBlank())
        assertNotNull(transactionId2.value())
        assertTrue(transactionId2.value().isNotBlank())
        assertNotEquals(transactionId01, transactionId2)
    }

    @Test
    fun givenTwoTransactionIdsWithTheSameValue_whenCompare_shouldBeEqual() {
        val id = IdUtils.generate()
        val transactionId01 = TransactionId.create(id)
        val transactionId2 = TransactionId.create(id)
        assertEquals(transactionId01, transactionId2)
        assertEquals(transactionId01.hashCode(), transactionId2.hashCode())
    }

    @Test
    fun givenTwoTransactionIdsWithDifferentValues_whenCompare_shouldNotBeEqual() {
        val transaction01 = TransactionId.create(IdUtils.generate())
        val transaction02 = TransactionId.create(IdUtils.generate())
        assertNotEquals(transaction01, transaction02)
    }

}
