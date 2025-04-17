package br.com.jaya.tech.infrastructure.transaction.persistence

import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.transaction.Transaction
import br.com.jaya.tech.domain.transaction.TransactionRepository
import br.com.jaya.tech.domain.transaction.vo.CurrencyType
import br.com.jaya.tech.infrastructure.common.database.DatabaseSupportIT
import br.com.jaya.tech.infrastructure.transaction.persistence.jpa.TransactionJpaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal

@Import(DefaultTransactionRepository::class)
@DisplayName("Integration test for Transaction Repository")
class TransactionRepositoryIT : DatabaseSupportIT() {

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var transactionJpaRepository: TransactionJpaRepository

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql"
    )
    fun givenAValidUser_whenCallSave_shouldPersistTransaction() {

        val registeredUserId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"

        val transactionToSave = Transaction.builder()
            .id(IdUtils.generate())
            .userId(registeredUserId)
            .originCurrency(CurrencyType.BRL.name)
            .originAmount(BigDecimal("100"))
            .destinationCurrency(CurrencyType.USD.name)
            .conversionRate(BigDecimal("0.1702273"))
            .build()

        val transactionCreated = transactionRepository.save(transactionToSave)
        assertNotNull(transactionCreated)

        val transactionFound = transactionJpaRepository.findByIdOrNull(transactionToSave.id().value())
        assertNotNull(transactionFound)

        assertEquals(transactionCreated.id().value(), transactionFound!!.id)
        assertEquals(transactionCreated.userId, transactionFound.user.id)
        assertEquals(transactionCreated.originMoney.currency, transactionFound.originCurrency)
        assertEquals(transactionCreated.originMoney.amount, transactionFound.originAmount)
        assertEquals(transactionCreated.destinationMoney.currency, transactionFound.destinationCurrency)
        assertNotNull(transactionCreated.destinationMoney.amount)
        assertEquals(transactionCreated.conversionRate, transactionFound.conversionRate)
        assertEquals(transactionCreated.createdAt, transactionFound.createdAt)

    }

    @ParameterizedTest(name = "list transactions of user {0} in page number {1}")
    @CsvSource(
        "85fc5f36-fe55-4d77-816d-8f6edfd395d5, 0",
        "85fc5f36-fe55-4d77-816d-8f6edfd395d5, 1",
        "a8ac2072-bb41-458f-8a75-aaefbd24a42b, 0",
        "a8ac2072-bb41-458f-8a75-aaefbd24a42b, 1",
    )
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql",
        "classpath:/sql/transaction/insert_transaction.sql"
    )
    fun givenAValidUserIdAsFilter_whenCallFindAll_shouldReturnATransactionPage(
        registeredUserId: String, expectedPageNumber: Int,
    ) {

        val expectedPageSize = 3
        val expectedNumberOfElements = 3
        val expectedTotalElements = 6
        val expectedTotalPages = 2

        val transactionPage = transactionRepository.findAll(registeredUserId, expectedPageNumber, expectedPageSize)

        assertNotNull(transactionPage)
        assertFalse(transactionPage.items.isEmpty())
        assertTrue(transactionPage.items.stream().allMatch { it.userId == registeredUserId } )

        assertEquals(expectedPageNumber, transactionPage.pageNumber)
        assertEquals(expectedPageSize, transactionPage.pageSize)
        assertEquals(expectedNumberOfElements, transactionPage.numberOfElements)
        assertEquals(expectedTotalElements, transactionPage.totalElements)
        assertEquals(expectedTotalPages, transactionPage.totalPages)

    }

}
