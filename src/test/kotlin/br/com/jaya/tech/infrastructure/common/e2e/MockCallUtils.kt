package br.com.jaya.tech.infrastructure.common.e2e

import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

object MockCallUtils {
    fun mockCall(
        method: HttpMethod,
        url: String,
        response: String,
        httpStatus: HttpStatus,
    ) = stubFor(
        request(method.name(), urlEqualTo(url)).willReturn(
            aResponse()
                .withStatus(httpStatus.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response),
        ),
    )
}
