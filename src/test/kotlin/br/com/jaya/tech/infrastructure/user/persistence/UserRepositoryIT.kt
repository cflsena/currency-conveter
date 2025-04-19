package br.com.jaya.tech.infrastructure.user.persistence

import br.com.jaya.tech.domain.common.utils.IdUtils
import br.com.jaya.tech.domain.user.User
import br.com.jaya.tech.domain.user.UserRepository
import br.com.jaya.tech.infrastructure.common.database.DatabaseSupportIT
import br.com.jaya.tech.infrastructure.user.persistence.jpa.UserJpaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql

@Import(DefaultUserRepository::class)
@DisplayName("Integration test for User Repository")
class UserRepositoryIT : DatabaseSupportIT() {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var jpaRepository: UserJpaRepository

    @Test
    @Sql("classpath:/sql/clear-db.sql")
    fun givenAValidUser_whenCallSave_shouldPersistUser() {
        val userCreated =
            userRepository.save(
                User
                    .builder()
                    .id(IdUtils.generate())
                    .givenName("John")
                    .familyName("Doe")
                    .email("test@test.com")
                    .build(),
            )

        assertNotNull(userCreated)

        val userFound = userRepository.findById(userCreated.id().value())

        assertNotNull(userFound)
        assertEquals(userCreated.id(), userFound!!.id())
        assertEquals(userCreated.name.givenName, userFound.name.givenName)
        assertEquals(userCreated.name.familyName, userFound.name.familyName)
        assertEquals(userCreated.email, userFound.email)
    }

    @Test
    @Sql("classpath:/sql/clear-db.sql")
    fun givenANotRegisteredUserId_whenCallFindById_shouldReturnNull() {
        val expectedUserId = "85fc5f36-fe55-4d77-816d-8f6edfd395d5"
        assertNull(userRepository.findById(expectedUserId))
    }

    @Sql("classpath:/sql/clear-db.sql")
    @ParameterizedTest(name = "{0} returns {1}")
    @CsvSource(
        "existsById, true, 85fc5f36-fe55-4d77-816d-8f6edfd395d5, test@test.com",
        "existsById, false, 85fc5f36-fe55-4d77-816d-8f6edfd395d5, test@test.com",
        "existsByEmail, true, 85fc5f36-fe55-4d77-816d-8f6edfd395d5, test@test.com",
        "existsByEmail, false, 85fc5f36-fe55-4d77-816d-8f6edfd395d5, test@test.com",
    )
    fun givenValidParams_whenCallExistsMethods_shouldReturnExpectedValue(
        method: String,
        expectedValue: Boolean,
        id: String,
        email: String,
    ) {
        if (expectedValue) {
            userRepository.save(
                User
                    .builder()
                    .id(id)
                    .givenName("John")
                    .familyName("Doe")
                    .email(email)
                    .build(),
            )
        }

        val result =
            when (method) {
                "existsById" -> userRepository.existsById(id)
                "existsByEmail" -> userRepository.existsByEmail(email)
                else -> error("Unknown method $method")
            }

        assertEquals(expectedValue, result)
        jpaRepository.deleteAll()
    }
}
