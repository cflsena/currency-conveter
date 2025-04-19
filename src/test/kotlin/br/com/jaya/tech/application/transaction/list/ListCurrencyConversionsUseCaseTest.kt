package br.com.jaya.tech.application.transaction.list

import br.com.jaya.tech.application.UseCaseTest
import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*
import java.math.BigDecimal
import java.util.*

@DisplayName("Unit Test for List Currency Conversions Use Case Test")
class ListCurrencyConversionsUseCaseTest : UseCaseTest() {
    @InjectMocks
    private lateinit var useCase: DefaultListCurrencyConversionsUseCase

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Test
    fun givenAnUserWithRegisteredTransactions_whenCallExecute_shouldReturnAPageOfTransactions() {
        val expectedUserId = IdUtils.generate()
        val expectedOriginAmount = BigDecimal("100.00")
        val expectedOriginCurrency = CurrencyType.BRL
        val expectedDestinationCurrency = CurrencyType.USD
        val expectedConversionRate = BigDecimal("0.1702273")
        val expectedDestinationAmount = BigDecimal("17.02")

        val expectedPageNumber = 0
        val expectedPageSize = 5
        val expectedNumberOfElements = 1
        val expectedTotalPages = 1
        val expectedTotalElements = 1

        val expectedTransaction =
            Transaction
                .builder()
                .id(expectedUserId)
                .userId(expectedUserId)
                .originAmount(expectedOriginAmount)
                .originCurrency(expectedOriginCurrency)
                .destinationCurrency(expectedDestinationCurrency)
                .conversionRate(expectedConversionRate)
                .build()

        val expectedTransactionPage =
            PageDTO(
                expectedPageNumber,
                expectedPageSize,
                expectedNumberOfElements,
                expectedTotalElements,
                expectedTotalPages,
                listOf(expectedTransaction),
            )

        val expectedOutput = CurrencyConversionsOutput.from(expectedTransaction)

        val input = CurrencyConversionsFilterInput(expectedUserId, expectedPageNumber, expectedPageSize)

        given(transactionRepository.findAll(any<UUID>(), any<Int>(), any<Int>())).willReturn(expectedTransactionPage)

        val output = useCase.execute(input)

        verify(transactionRepository, times(1)).findAll(
            argThat { userId -> userId == UUID.fromString(input.userId) },
            argThat { pageNumber -> pageNumber == input.pageNumber },
            argThat { pageSize -> pageSize == input.pageSize },
        )

        assertNotNull(output.items)
        assertFalse(output.items.isEmpty())
        assertEquals(expectedPageNumber, output.pageNumber)
        assertEquals(expectedPageSize, output.pageSize)
        assertEquals(expectedNumberOfElements, output.numberOfElements)
        assertEquals(expectedTotalPages, output.totalPages)
        assertEquals(expectedTotalElements, output.totalElements)
        assertEquals(expectedOutput, output.items[0])
        assertEquals(expectedDestinationAmount, output.items[0].destinationAmount)
    }

    @Test
    fun givenAnUserWithoutRegisteredTransactions_whenCallExecute_shouldNotReturnAPageOfTransactions() {
        val expectedUserId = IdUtils.generate()

        val expectedPageNumber = 0
        val expectedPageSize = 5
        val expectedNumberOfElements = 1
        val expectedTotalPages = 1
        val expectedTotalElements = 1

        val expectedTransactionPage =
            PageDTO(
                expectedPageNumber,
                expectedPageSize,
                expectedNumberOfElements,
                expectedTotalElements,
                expectedTotalPages,
                listOf<Transaction>(),
            )

        val input = CurrencyConversionsFilterInput(expectedUserId, expectedPageNumber, expectedPageSize)

        given(transactionRepository.findAll(any<UUID>(), any<Int>(), any<Int>())).willReturn(expectedTransactionPage)

        val output = useCase.execute(input)

        verify(transactionRepository, times(1)).findAll(
            argThat { userId -> userId == UUID.fromString(input.userId) },
            argThat { pageNumber -> pageNumber == input.pageNumber },
            argThat { pageSize -> pageSize == input.pageSize },
        )

        assertNotNull(output.items)
        assertTrue(output.items.isEmpty())
        assertEquals(expectedPageNumber, output.pageNumber)
        assertEquals(expectedPageSize, output.pageSize)
        assertEquals(expectedNumberOfElements, output.numberOfElements)
        assertEquals(expectedTotalPages, output.totalPages)
        assertEquals(expectedTotalElements, output.totalElements)
    }
}
