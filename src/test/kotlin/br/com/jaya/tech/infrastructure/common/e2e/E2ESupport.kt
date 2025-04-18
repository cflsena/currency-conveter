package br.com.jaya.tech.infrastructure.common.e2e

import br.com.jaya.tech.CurrencyConverterApplication
import br.com.jaya.tech.infrastructure.common.database.PostgresContainerTest
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles

@PostgresContainerTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [CurrencyConverterApplication::class]
)
class E2ESupport {

    @Autowired
    lateinit var context: ServletWebServerApplicationContext

    @BeforeEach
    fun setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        RestAssured.port = context.webServer.port
    }

}