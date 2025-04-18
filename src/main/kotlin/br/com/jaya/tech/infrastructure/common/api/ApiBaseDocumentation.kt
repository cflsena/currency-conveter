package br.com.jaya.tech.infrastructure.common.api

import br.com.jaya.tech.shared.exception.Error
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType

@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "422",
            description = "A validation error was thrown",
            content = [
                Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Error::class)
                )
            ]
        ),
        ApiResponse(
            responseCode = "500",
            description = "An internal server error was thrown",
            content = [
                Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Error::class)
                )
            ]
        ),
    ]
)
interface ApiBaseDocumentation {
}