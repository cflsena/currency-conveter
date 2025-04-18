package br.com.jaya.tech.infrastructure.transaction.controller

import br.com.jaya.tech.domain.common.pagination.PageDTO
import br.com.jaya.tech.infrastructure.common.e2e.E2ESupport
import br.com.jaya.tech.infrastructure.common.e2e.JsonTestUtils.readJsonAsString
import br.com.jaya.tech.infrastructure.common.e2e.MockCallUtils.mockCall
import br.com.jaya.tech.infrastructure.transaction.api.CurrencyConversionResponse
import br.com.jaya.tech.infrastructure.transaction.persistence.jpa.TransactionJpaRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("Integration test for Transaction Controller")
class TransactionControllerIT : E2ESupport() {

    @Autowired
    lateinit var transactionRepository: TransactionJpaRepository

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    companion object {
        const val BASE_PATH = "api/v1/jaya-tech/transactions"
    }

    @ParameterizedTest(name = "conversion of {1} from {0} to {3}")
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql"
    )
    @CsvSource(
        "BRL;100.00;R$ 100,00;USD;17.02;$17.02;0.1702273",
        "USD;17.02;$17.02;BRL;99.98;R$ 99,98;5.8744994",
        "BRL;100.00;R$ 100,00;EUR;14.96;14,96 €;0.1495591",
        "EUR;14.96;14,96 €;BRL;100.03;R$ 100,03;6.6863200",
        "BRL;100.00;R$ 100,00;JPY;2427.09;￥2,427;24.2709152",
        "JPY;2427.09;￥2,427;BRL;100.00;R$ 100,00;0.0412016",
        "USD;100.00;$100.00;EUR;87.86;87,86 €;0.8785848",
        "EUR;87.86;87,86 €;USD;100.00;$100.00;1.1381940",
        "USD;100.00;$100.00;JPY;14257.95;￥14,258;142.5794777",
        "JPY;14257.95;￥14,258;USD;100.00;$100.00;0.0070136",
        "EUR;100.00;100,00 €;JPY;16228.31;￥16,228;162.2831060",
        "JPY;16228.31;￥16,228;EUR;100.00;100,00 €;0.0061621",

        delimiter = ';'
    )
    fun givenAValidRequest_whenCallCreateCurrencyConversion_shouldReturnConversion(
        expectedOriginCurrency: String,
        expectedOriginAmount: BigDecimal,
        expectedOriginAmountFormatted: String,
        expectedDestinationCurrency: String,
        expectedDestinationAmount: BigDecimal,
        expectedDestinationAmountFormatted: String,
        expectedConversionRate: BigDecimal
    ) {

        val expectedUserId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"

        assertTrue(transactionRepository.findAll().isEmpty())

        mockCall(
            HttpMethod.GET,
            "/v1/latest?access_key=123456&base=EUR",
            readJsonAsString("transaction/exchange_rates_response.json"),
            HttpStatus.OK
        )

        val requestBody = readJsonAsString("transaction/create_currency_conversion_request.json")
            .replace("<<origin>>", expectedOriginCurrency)
            .replace("<<destination>>", expectedDestinationCurrency)
            .replace("<<amount>>", expectedOriginAmount.toString())

        val response = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(BASE_PATH)

        response.then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .log()
            .all()

        assertNotNull(response)

        val responseConverted: CurrencyConversionResponse = objectMapper.readValue(response.body().asString())

        val transactionFound = transactionRepository.findById(responseConverted.id)

        assertTrue(transactionFound.isPresent)
        assertNotNull(transactionFound.get().createdAt)
        assertEquals(expectedUserId, transactionFound.get().user.id)
        assertEquals(expectedOriginCurrency, transactionFound.get().originCurrency.name)
        assertEquals(expectedOriginAmount, transactionFound.get().originAmount)
        assertEquals(expectedDestinationCurrency, transactionFound.get().destinationCurrency.name)
        assertEquals(expectedConversionRate, transactionFound.get().conversionRate)

        assertNotNull(responseConverted.id)
        assertNotNull(responseConverted.createdAt)
        assertEquals(expectedUserId, responseConverted.userId)
        assertEquals(expectedOriginCurrency, responseConverted.originCurrency)
        assertEquals(expectedOriginAmount, responseConverted.originAmount)
        assertEquals(expectedOriginAmountFormatted, responseConverted.originAmountFormatted)
        assertEquals(expectedDestinationCurrency, responseConverted.destinationCurrency)
        assertEquals(expectedDestinationAmount, responseConverted.destinationAmount)
        assertEquals(expectedDestinationAmountFormatted, responseConverted.destinationAmountFormatted)
        assertEquals(expectedConversionRate, responseConverted.conversionRate)

    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql",
        "classpath:/sql/transaction/insert_transaction.sql"
    )
    fun givenAValidUserWithConversions_whenCallListCurrencyConversions_shouldReturnPageOfConversions() {

        val expectedUserId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"
        val expectedPageNumber = 0
        val expectedPageSize = 3
        val expectedNumberOfElements = 3
        val expectedTotalElements = 6
        val expectedTotalPages = 2

        val response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("$BASE_PATH/filter?userId=$expectedUserId&pageNumber=$expectedPageNumber&pageSize=$expectedPageSize")

        response.then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .log()
            .all()

        val responseConverted: PageDTO<CurrencyConversionResponse> = objectMapper.readValue(response.body().asString())

        assertFalse(responseConverted.items.isEmpty())
        assertEquals(expectedPageNumber, responseConverted.pageNumber)
        assertEquals(expectedPageSize, responseConverted.pageSize)
        assertEquals(expectedNumberOfElements, responseConverted.numberOfElements)
        assertEquals(expectedTotalElements, responseConverted.totalElements)
        assertEquals(expectedTotalPages, responseConverted.totalPages)
        responseConverted.items.forEach { assertEquals(it.userId, expectedUserId) }

    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql"
    )
    fun givenAValidUserWithoutConversions_whenCallListCurrencyConversions_shouldReturnEmptyPage() {

        val expectedUserId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"
        val expectedPageNumber = 0
        val expectedPageSize = 3
        val expectedNumberOfElements = 0
        val expectedTotalElements = 0
        val expectedTotalPages = 0

        val response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("$BASE_PATH/filter?userId=$expectedUserId&pageNumber=$expectedPageNumber&pageSize=$expectedPageSize")

        response.then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .log()
            .all()

        val responseConverted: PageDTO<CurrencyConversionResponse> = objectMapper.readValue(response.body().asString())

        assertTrue(responseConverted.items.isEmpty())
        assertEquals(expectedPageNumber, responseConverted.pageNumber)
        assertEquals(expectedPageSize, responseConverted.pageSize)
        assertEquals(expectedNumberOfElements, responseConverted.numberOfElements)
        assertEquals(expectedTotalElements, responseConverted.totalElements)
        assertEquals(expectedTotalPages, responseConverted.totalPages)

    }

    @Test
    @Sql("classpath:/sql/clear-db.sql")
    fun givenANotRegisteredUser_whenCallListCurrencyConversions_shouldReturnEmptyPage() {

        val expectedUserId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"
        val expectedPageNumber = 0
        val expectedPageSize = 3
        val expectedNumberOfElements = 0
        val expectedTotalElements = 0
        val expectedTotalPages = 0

        val response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("$BASE_PATH/filter?userId=$expectedUserId&pageNumber=$expectedPageNumber&pageSize=$expectedPageSize")

        response.then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .log()
            .all()

        val responseConverted: PageDTO<CurrencyConversionResponse> = objectMapper.readValue(response.body().asString())

        assertTrue(responseConverted.items.isEmpty())
        assertEquals(expectedPageNumber, responseConverted.pageNumber)
        assertEquals(expectedPageSize, responseConverted.pageSize)
        assertEquals(expectedNumberOfElements, responseConverted.numberOfElements)
        assertEquals(expectedTotalElements, responseConverted.totalElements)
        assertEquals(expectedTotalPages, responseConverted.totalPages)

    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql"
    )
    fun givenAnUserNotRegistered_whenCallCreateCurrencyConversion_shouldThrowsAnException() {

        val userId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"

        val requestBody = readJsonAsString("transaction/create_currency_conversion_request.json")
            .replace("<<origin>>", "BRL")
            .replace("<<destination>>", "USD")
            .replace("<<amount>>", "100")

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(BASE_PATH)
            .then()
            .assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("message", equalTo("Unable to perform conversion, user with id $userId not found"))
            .log()
            .all()

    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql"
    )
    fun givenTheSameCurrencies_whenCallCreateCurrencyConversion_shouldThrowsAnException() {

        given()
            .contentType(ContentType.JSON)
            .body(readJsonAsString("transaction/create_conversion_same_currencies_request.json"))
            .`when`()
            .post(BASE_PATH)
            .then()
            .assertThat()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .body("message", equalTo("Unable to perform conversion, please inform two different currencies"))
            .log()
            .all()

    }

    @ParameterizedTest(name = "invalid origin {0} throws message {1}")
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql"
    )
    @CsvSource(
        "BRL;USD;USD;Origin currency not available for conversion. Please select another one",
        "BRL;USD;BRL;Destination currency not available for conversion. Please select another one",
        delimiter = ';'
    )
    fun givenInvalidCurrencies_whenCallCreateCurrencyConversion_shouldThrowsAnException(
        originCurrency: String, destinationCurrency: String, availableCurrency: String, errorMessage: String
    ) {

        val requestBody = readJsonAsString("transaction/create_conversion_invalid_currencies_request.json")
            .replace("<<origin>>", originCurrency)
            .replace("<<destination>>", destinationCurrency)

        val exchangeRatesMockResponse = readJsonAsString("transaction/invalid_exchange_rates_response.json")
            .replace("<<currency>>", availableCurrency)

        mockCall(
            HttpMethod.GET,
            "/v1/latest?access_key=123456&base=EUR",
            exchangeRatesMockResponse,
            HttpStatus.OK
        )

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(BASE_PATH)
            .then()
            .assertThat()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .body("message", equalTo(errorMessage))
            .log()
            .all()

    }

    @ParameterizedTest
    @CsvSource(
        """
            {
              "originCurrency": "BRL",
              "originAmount": 100,
              "destinationCurrency": "USD"
            }
        """,
        """
            {
              "userId": "85fc5f36-fe55-4d77-816d-8f6edfd395d5",
              "originAmount": 100,
              "destinationCurrency": "USD"
            }
        """,
        """
            {
              "userId": "85fc5f36-fe55-4d77-816d-8f6edfd395d5",
              "originCurrency": "BRL",
              "destinationCurrency": "USD"
            }
        """,
        """
            {
              "userId": "85fc5f36-fe55-4d77-816d-8f6edfd395d5",
              "originCurrency": "BRL",
              "originAmount": 100,
            }
        """
    )
    @Sql("classpath:/sql/clear-db.sql")
    fun givenAnInvalidRequest_whenCallCreateCurrencyConversion_shouldThrowsAnException(invalidRequest: String) {
        given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
            .`when`()
            .post(BASE_PATH)
            .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("message", equalTo("Invalid Request. Please check payload request"))
            .log()
            .all()
    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql"
    )
    fun givenAValidWhenExternalRatesServiceIsUnavailable_whenCallCreateCurrencyConversion_shouldThrowsAnException() {

        val requestBody = readJsonAsString("transaction/create_currency_conversion_request.json")
            .replace("<<origin>>", "BRL")
            .replace("<<destination>>", "USD")
            .replace("<<amount>>", "100")

        mockCall(
            HttpMethod.GET,
            "/v1/latest?access_key=123456&base=EUR",
            "",
            HttpStatus.SERVICE_UNAVAILABLE
        )

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(BASE_PATH)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SERVICE_UNAVAILABLE.value())
            .body("message", equalTo("Unable to process request. Please try again later"))
            .log()
            .all()

    }

}