package br.com.jaya.tech.application.user.update

import br.com.jaya.tech.application.UseCaseTest
import br.com.jaya.tech.domain.common.exception.NotFoundException
import br.com.jaya.tech.domain.common.exception.ResourceAlreadyCreatedException
import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.AdditionalAnswers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*
import java.util.*
import kotlin.test.assertNotEquals

@DisplayName("Unit Test for Update User Account Use Case")
class UpdateUserAccountUseCaseTest : UseCaseTest() {
    @InjectMocks
    private lateinit var useCase: DefaultUpdateUserAccountUseCase

    @Mock
    private lateinit var userRepository: UserRepository

    @ParameterizedTest
    @CsvSource(
        "Daenerys; Targaryen; dan@targaryen.com",
        "Daenerys; Targaryen; test@test.com",
        "Daenerys; Targaryen;''",
        delimiter = ';',
    )
    fun givenAValidInput_whenCallExecute_shouldCreateUserAccount(
        newGivenName: String,
        newFamilyName: String,
        newEmail: String,
    ) {
        val expectedUser =
            User
                .builder()
                .givenName("John")
                .familyName("Snow")
                .email("test@test.com")
                .build()

        val input = UpdateUserAccountInput(IdUtils.generate(), newGivenName, newFamilyName, newEmail)

        given(userRepository.findById(any<UUID>())).willReturn(expectedUser)
        given(userRepository.save(any())).will(AdditionalAnswers.returnsFirstArg<User>())

        Assertions.assertDoesNotThrow { useCase.execute(input) }

        verify(userRepository, times(1)).save(
            argThat { user ->
                newGivenName == user.name.givenName &&
                    newFamilyName == user.name.familyName &&
                    (newEmail.isBlank() || newEmail == user.email.value)
            },
        )
    }

    @Test
    fun givenAnUserNotRegistered_whenCallExecute_shouldThrowsAnException() {
        val expectedErrorMessage = "Failed to process request. Please, try again later"

        val input = UpdateUserAccountInput(IdUtils.generate(), "John", "Snow", "test@test.com")

        given(userRepository.findById(any<UUID>())).willReturn(null)

        val exception =
            Assertions.assertThrows(
                NotFoundException::class.java,
            ) { useCase.execute(input) }

        assertEquals(expectedErrorMessage, exception.message)
    }

    @Test
    fun givenAnUserAlreadyCreated_whenCallExecute_shouldThrowsAnException() {
        val expectedErrorMessage = "Failed to process request. Please, try again later"
        val expectedUser =
            User
                .builder()
                .givenName("John")
                .familyName("Snow")
                .email("test@test.com")
                .build()

        val input = UpdateUserAccountInput(IdUtils.generate(), "Daenerys", "Targaryen", "test2@test.com")

        assertNotEquals(expectedUser.id().value(), input.id)

        given(userRepository.findById(any<UUID>())).willReturn(expectedUser)
        given(userRepository.existsByEmail(any<String>())).willReturn(true)

        val exception =
            Assertions.assertThrows(
                ResourceAlreadyCreatedException::class.java,
            ) { useCase.execute(input) }

        assertEquals(expectedErrorMessage, exception.message)
    }
}
