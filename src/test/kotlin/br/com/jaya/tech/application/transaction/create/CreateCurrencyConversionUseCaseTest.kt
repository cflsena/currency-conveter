package br.com.jaya.tech.application.transaction.create

import br.com.jaya.tech.application.UseCaseTest
import br.com.jaya.tech.application.service.ConversionRateService
import br.com.jaya.tech.domain.common.exception.BusinessException
import br.com.jaya.tech.domain.common.exception.NotFoundException
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import br.com.jaya.tech.domain.user.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*
import java.math.BigDecimal

@DisplayName("Unit Test for Create Currency Conversion Use Case Test")
class CreateCurrencyConversionUseCaseTest : UseCaseTest() {

    @InjectMocks
    private lateinit var useCase: DefaultCreateCurrencyConversionUseCase

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var conversionRateService: ConversionRateService

    @Test
    fun givenAValidInput_whenCallExecute_shouldCreateTransactionCurrencyConversion() {

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL.name
        val expectedOriginConversionRate = Pair(expectedOriginCurrency, BigDecimal("6.68632"))
        val expectedDestinationCurrency = CurrencyType.USD.name
        val expectedDestinationConversionRate = Pair(expectedDestinationCurrency, BigDecimal("1.138194"))
        val expectedRates = mapOf(expectedOriginConversionRate, expectedDestinationConversionRate)
        val expectedConversionRate = BigDecimal("0.1702273")
        val expectedDestinationAmount = BigDecimal("17.02")

        val input = CreateCurrencyConversionInput(
            expectedUserId,
            expectedOriginAmount,
            expectedOriginCurrency,
            expectedDestinationCurrency
        )

        given(userRepository.existsById(any<String>())).willReturn(true)
        given(conversionRateService.getRates()).willReturn(expectedRates)
        given(transactionRepository.save(any())).will(AdditionalAnswers.returnsFirstArg<Transaction>())

        val output = useCase.execute(input)

        assertNotNull(output)
        assertNotNull(output.id)
        assertNotNull(output.createdAt)
        assertEquals(expectedUserId, output.userId)
        assertEquals(expectedOriginCurrency, output.originCurrency)
        assertEquals(expectedOriginAmount, output.originAmount)
        assertEquals(expectedDestinationCurrency, output.destinationCurrency)
        assertEquals(expectedDestinationAmount, output.destinationAmount)
        assertEquals(expectedConversionRate, output.conversionRate)

        verify(userRepository, times(1)).existsById(argThat { userId -> userId == expectedUserId })

        verify(transactionRepository, times(1)).save(argThat { transaction ->
            expectedUserId == transaction.userId &&
            expectedOriginCurrency == transaction.originMoney.currency.name &&
            expectedOriginAmount == transaction.originMoney.amount &&
            expectedDestinationCurrency == transaction.destinationMoney.currency.name &&
            expectedDestinationAmount == transaction.destinationMoney.amount &&
            expectedConversionRate == transaction.conversionRate
        })

    }

    @Test
    fun givenTheSameCurrenciesAsInput_whenCallExecute_shouldThrowsAnException() {

        val expectedErrorMessage = "Unable to perform conversion, please inform two different currencies"

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL.name
        val expectedDestinationCurrency = CurrencyType.BRL.name

        val input = CreateCurrencyConversionInput(
            expectedUserId,
            expectedOriginAmount,
            expectedOriginCurrency,
            expectedDestinationCurrency
        )

        given(userRepository.existsById(any<String>())).willReturn(true)

        val exception = Assertions.assertThrows(
            BusinessException::class.java
        ) { useCase.execute(input) }

        verifyNoInteractions(conversionRateService, transactionRepository)
        assertEquals(expectedErrorMessage, exception.message)

    }

    @Test
    fun givenANotRegisteredUser_whenCallExecute_shouldThrowsAnException() {

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL.name
        val expectedDestinationCurrency = CurrencyType.USD.name

        val expectedErrorMessage = "Unable to perform conversion, user with id $expectedUserId not found"

        val input = CreateCurrencyConversionInput(
            expectedUserId,
            expectedOriginAmount,
            expectedOriginCurrency,
            expectedDestinationCurrency
        )

        given(userRepository.existsById(any<String>())).willReturn(false)

        val exception = Assertions.assertThrows(
            NotFoundException::class.java
        ) { useCase.execute(input) }

        verifyNoInteractions(conversionRateService, transactionRepository)
        assertEquals(expectedErrorMessage, exception.message)

    }

    @Test
    fun givenANotAvailableOriginCurrency_whenCallExecute_shouldThrowsAnException() {

        val expectedErrorMessage = "Origin currency not available for conversion. Please select another one"

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL.name
        val expectedDestinationCurrency = CurrencyType.USD.name

        val expectedOriginConversionRate = Pair(CurrencyType.JPY.name, BigDecimal("6.68632"))
        val expectedDestinationConversionRate = Pair(expectedDestinationCurrency, BigDecimal("1.138194"))
        val expectedRates = mapOf(expectedOriginConversionRate, expectedDestinationConversionRate)

        val input = CreateCurrencyConversionInput(
            expectedUserId,
            expectedOriginAmount,
            expectedOriginCurrency,
            expectedDestinationCurrency
        )

        given(userRepository.existsById(any<String>())).willReturn(true)
        given(conversionRateService.getRates()).willReturn(expectedRates)

        val exception = Assertions.assertThrows(
            BusinessException::class.java
        ) { useCase.execute(input) }

        assertEquals(expectedErrorMessage, exception.message)
        verifyNoInteractions(transactionRepository)

    }

    @Test
    fun givenANotAvailableDestinationCurrency_whenCallExecute_shouldThrowsAnException() {

        val expectedErrorMessage = "Destination currency not available for conversion. Please select another one"

        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL.name
        val expectedDestinationCurrency = CurrencyType.USD.name

        val expectedOriginConversionRate = Pair(expectedOriginCurrency, BigDecimal("6.68632"))
        val expectedDestinationConversionRate = Pair(CurrencyType.JPY.name, BigDecimal("1.138194"))
        val expectedRates = mapOf(expectedOriginConversionRate, expectedDestinationConversionRate)

        val input = CreateCurrencyConversionInput(
            expectedUserId,
            expectedOriginAmount,
            expectedOriginCurrency,
            expectedDestinationCurrency
        )

        given(userRepository.existsById(any<String>())).willReturn(true)
        given(conversionRateService.getRates()).willReturn(expectedRates)

        val exception = Assertions.assertThrows(
            BusinessException::class.java
        ) { useCase.execute(input) }

        assertEquals(expectedErrorMessage, exception.message)
        verifyNoInteractions(transactionRepository)

    }

}
