package br.com.jaya.tech.application

import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations.openMocks

open class UseCaseTest {

    @BeforeEach
    fun setup() {
        openMocks(this)
    }

}