package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.exception.DomainException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DisplayName("Unit Test for User Entity")
class UserTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "85fc5f36-fe55-4d77-816d-8f6edfd395d5"])
    fun givenValidParams_whenCallBuilder_shouldInstantiateUser(expectedId: String) {

        val expectedGivenName = "John"
        val expectedFamilyName = "Doe"
        val expectedEmail = "test@test.com"

        val userCreated = User.builder()
            .id(expectedId)
            .givenName(expectedGivenName)
            .familyName(expectedFamilyName)
            .email(expectedEmail)
            .build();

        assertNotNull(userCreated.id)
        assertNotNull(userCreated.id.value)

        if(expectedId.isNotBlank()) assertNotNull(expectedId, userCreated.id.value)

        assertEquals(expectedGivenName, userCreated.name.givenName)
        assertEquals(expectedFamilyName, userCreated.name.familyName)
        assertEquals(expectedEmail, userCreated.email.value)

    }

    @ParameterizedTest
    @ValueSource(strings = ["", "ac"])
    fun givenAnInvalidGivenName_whenCallBuilder_shouldThrowsAnException(invalidGivenName: String) {

        val expectedErrorMessage01 = "'givenName' should not be null or empty"
        val expectedErrorMessage02 = "'givenName' invalid size, min size is 3"
        val expectedFamilyName = "Doe"
        val expectedEmail = "test@test.com"

        val exception = assertThrows(
            DomainException::class.java,
        ) {
            User.builder()
                .givenName(invalidGivenName)
                .familyName(expectedFamilyName)
                .email(expectedEmail)
                .build()
        }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @ParameterizedTest
    @ValueSource(strings = ["", "ac"])
    fun givenAnInvalidFamilyName_whenCallBuilder_shouldThrowsAnException(invalidFamilyName: String) {

        val expectedErrorMessage01 = "'familyName' should not be null or empty"
        val expectedErrorMessage02 = "'familyName' invalid size, min size is 3"
        val expectedGivenName = "Doe"
        val expectedEmail = "test@test.com"

        val exception = assertThrows(
            DomainException::class.java,
        ) {
            User.builder()
                .givenName(expectedGivenName)
                .familyName(invalidFamilyName)
                .email(expectedEmail)
                .build()
        }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

    @ParameterizedTest
    @ValueSource(strings = ["", "ac", "test@", "test@test"])
    fun givenAnInvalidEmail_whenCallBuilder_shouldThrowsAnException(invalidEmail: String) {

        val expectedErrorMessage01 = "'email' should not be null or empty"
        val expectedErrorMessage02 = "'email' is not a valid email format"
        val expectedGivenName = "John"
        val expectedFamilyName = "Doe"

        val exception = assertThrows(
            DomainException::class.java,
        ) {
            User.builder()
                .givenName(expectedGivenName)
                .familyName(expectedFamilyName)
                .email(invalidEmail)
                .build()
        }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))

    }

}