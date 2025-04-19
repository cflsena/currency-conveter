package br.com.jaya.tech.infrastructure.user.controller

import br.com.jaya.tech.infrastructure.common.e2e.E2ESupport
import br.com.jaya.tech.infrastructure.common.e2e.JsonTestUtils.readJsonAsString
import br.com.jaya.tech.infrastructure.user.api.UserAccountResponse
import br.com.jaya.tech.infrastructure.user.persistence.jpa.UserJpaRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DisplayName("Integration test for User Controller")
class UserControllerIT : E2ESupport() {
    @Autowired
    lateinit var userRepository: UserJpaRepository

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    companion object {
        const val BASE_PATH = "api/v1/jaya-tech/users"
    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
    )
    fun givenAValidRequest_whenCallCreateUserAccount_shouldReturnUser() {
        val expectedGivenName = "John"
        val expectedFamilyName = "Doe"
        val expectedEmail = "test@test.com"

        val requestBody = readJsonAsString("user/create_user_account_request.json")

        assertTrue(userRepository.findAll().isEmpty())

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`()
                .post(BASE_PATH)

        response
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .log()
            .all()

        assertNotNull(response)

        val responseConverted: UserAccountResponse = objectMapper.readValue(response.body().asString())
        val userFound = userRepository.findById(responseConverted.id)

        assertTrue(userFound.isPresent)
        assertEquals(expectedGivenName, userFound.get().givenName)
        assertEquals(expectedFamilyName, userFound.get().familyName)
        assertEquals(expectedEmail, userFound.get().email)
    }

    @Test
    @Sql(
        "classpath:/sql/clear-db.sql",
        "classpath:/sql/user/insert_user.sql",
    )
    fun givenAnEmailAlreadyRegistered_whenCallCreateUserAccount_shouldThrowsAnException() {
        val requestBody = readJsonAsString("user/create_user_account_request.json")

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post(BASE_PATH)
            .then()
            .assertThat()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .body("message", equalTo("Failed to process request. Please, try again later"))
            .log()
            .all()
    }

    @ParameterizedTest
    @CsvSource(
        """
            {
              "familyName": "Doe",
              "email": "test@test.com"
            };
        """,
        """
            {
              "givenName": "John",
              "email": "test@test.com"
            }
        """,
        """
            {
              "givenName": "John",
              "familyName": "Doe",
            }
        """,
    )
    @Sql("classpath:/sql/clear-db.sql")
    fun givenAnInvalidRequest_whenCallCreateUserAccount_shouldThrowsAnException(invalidRequest: String) {
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
}
