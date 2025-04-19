package br.com.jaya.tech.domain.user

import br.com.jaya.tech.domain.common.exception.DomainException
import br.com.jaya.tech.domain.common.utils.IdUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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

        val userCreated =
            User
                .builder()
                .id(expectedId)
                .givenName(expectedGivenName)
                .familyName(expectedFamilyName)
                .email(expectedEmail)
                .build()

        assertNotNull(userCreated.id())
        assertNotNull(userCreated.id().value())

        if (expectedId.isNotBlank()) assertEquals(expectedId, userCreated.id().value())

        assertEquals(expectedGivenName, userCreated.name.givenName)
        assertEquals(expectedFamilyName, userCreated.name.familyName)
        assertEquals(expectedEmail, userCreated.email.value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "ac"])
    fun givenAnInvalidGivenName_whenCallBuilder_shouldThrowsAnException(invalidGivenName: String) {
        val expectedErrorMessage01 = "'givenName' should not be null or empty"
        val expectedErrorMessage02 = "'givenName' invalid size, should be between 3 and 50"
        val expectedFamilyName = "Doe"
        val expectedEmail = "test@test.com"

        val exception =
            assertThrows(
                DomainException::class.java,
            ) {
                User
                    .builder()
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
        val expectedErrorMessage02 = "'familyName' invalid size, should be between 3 and 50"
        val expectedGivenName = "Doe"
        val expectedEmail = "test@test.com"

        val exception =
            assertThrows(
                DomainException::class.java,
            ) {
                User
                    .builder()
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

        val exception =
            assertThrows(
                DomainException::class.java,
            ) {
                User
                    .builder()
                    .givenName(expectedGivenName)
                    .familyName(expectedFamilyName)
                    .email(invalidEmail)
                    .build()
            }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))
    }

    @Test
    fun givenAnInvalidEmail_whenCallBuilder_shouldThrowsAnException() {
        val expectedErrorMessage = "'email' invalid size, max size is 50"
        val expectedGivenName = "John"
        val expectedFamilyName = "Doe"

        val exception =
            assertThrows(
                DomainException::class.java,
            ) {
                User
                    .builder()
                    .givenName(expectedGivenName)
                    .familyName(expectedFamilyName)
                    .email("invalid_email_test_test_0001@invalid_email_test.com")
                    .build()
            }

        assertEquals(expectedErrorMessage, exception.message)
    }

    @Test
    fun givenAValidEmail_whenCallUpdateEmail_shouldUpdateUserEmail() {
        val expectedNewEmail = "test01@test.com"
        val expectedUser =
            User
                .builder()
                .givenName("John")
                .familyName("Doe")
                .email("test@test.com")
                .build()

        expectedUser.updateEmail(expectedNewEmail)

        assertEquals(expectedNewEmail, expectedUser.email.value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "ac", "test@", "test@test"])
    fun givenAnInvalidEmail_whenCallUpdateEmail_shouldThrowsAnException(invalidEmail: String) {
        val expectedErrorMessage01 = "'email' should not be null or empty"
        val expectedErrorMessage02 = "'email' is not a valid email format"

        val expectedUser =
            User
                .builder()
                .givenName("John")
                .familyName("Doe")
                .email("test@test.com")
                .build()

        val exception = assertThrows(DomainException::class.java) { expectedUser.updateEmail(invalidEmail) }

        assertTrue(exception.message.equals(expectedErrorMessage01) || exception.message.equals(expectedErrorMessage02))
    }

    @Test
    fun givenAValidUser_whenCallGetFormattedName_shouldReturnUserNameFormatted() {
        val expectedFormattedName = "John Doe"

        val expectedGivenName = "John"
        val expectedFamilyName = "Doe"
        val expectedEmail = "test@test.com"

        val user =
            User
                .builder()
                .givenName(expectedGivenName)
                .familyName(expectedFamilyName)
                .email(expectedEmail)
                .build()

        assertEquals(expectedFormattedName, user.getFormattedName())
    }

    @Test
    fun givenTwoUserIds_whenCallCreate_shouldHaveTwoUniqueAndNonEmptyIds() {
        val userId01 = UserId.create()
        val userIdd2 = UserId.create()
        assertNotNull(userId01.value())
        assertTrue(userId01.value().isNotBlank())
        assertNotNull(userIdd2.value())
        assertTrue(userIdd2.value().isNotBlank())
        assertNotEquals(userId01, userIdd2)
    }

    @Test
    fun givenTwoUserIdsWithTheSameValue_whenCompare_shouldBeEqual() {
        val id = IdUtils.generate()
        val userId01 = UserId.create(id)
        val userIdd2 = UserId.create(id)
        assertEquals(userId01, userIdd2)
        assertEquals(userId01.hashCode(), userIdd2.hashCode())
    }

    @Test
    fun givenTwoUserIdsWithDifferentValues_whenCompare_shouldNotBeEqual() {
        val userId01 = UserId.create(IdUtils.generate())
        val userIdd2 = UserId.create(IdUtils.generate())
        assertNotEquals(userId01, userIdd2)
    }
}
