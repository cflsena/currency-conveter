package br.com.jaya.tech.application.user.create

import br.com.jaya.tech.application.UseCaseTest
import br.com.jaya.tech.domain.common.exception.ResourceAlreadyCreatedException
import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.*

@DisplayName("Unit Test for Create User Account Use Case")
class CreateUserAccountUseCaseTest : UseCaseTest() {
    @InjectMocks
    private lateinit var useCase: DefaultCreateUserAccountUseCase

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun givenAValidInput_whenCallExecute_shouldCreateUserAccount() {
        val expectedGivenName = "John"
        val expectedFamilyName = "Doe"
        val expectedEmail = "test@test.com"

        val input =
            CreateUserAccountInput(
                givenName = expectedGivenName,
                familyName = expectedFamilyName,
                email = expectedEmail,
            )

        given(userRepository.save(any())).will(returnsFirstArg<User>())

        val output = useCase.execute(input)

        assertNotNull(output)
        assertNotNull(output.id)

        verify(userRepository, times(1)).save(
            argThat { user ->
                expectedGivenName == user.name.givenName &&
                    expectedFamilyName == user.name.familyName &&
                    expectedEmail == user.email.value
            },
        )
    }

    @Test
    fun givenAnUserAlreadyRegistered_whenCallExecute_shouldThrowsAnException() {
        val expectedErrorMessage = "Failed to process request. Please, try again later"

        val input = CreateUserAccountInput("John", "Doe", "test@test.com")

        given(userRepository.existsByEmail(any<String>())).willReturn(true)

        val exception =
            Assertions.assertThrows(
                ResourceAlreadyCreatedException::class.java,
            ) { useCase.execute(input) }

        assertEquals(expectedErrorMessage, exception.message)
    }
}
