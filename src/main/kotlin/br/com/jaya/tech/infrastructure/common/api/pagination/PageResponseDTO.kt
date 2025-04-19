package br.com.jaya.tech.infrastructure.common.api.pagination

import io.swagger.v3.oas.annotations.media.Schema

class PageResponseDTO<T>(
    @Schema(description = "Page index (pages start at position 0)", example = "0")
    val pageNumber: Int,
    @Schema(description = "Number of elements expected on the page", example = "5")
    val pageSize: Int,
    @Schema(description = "Number of elements on the current page", example = "5")
    val numberOfElements: Int,
    @Schema(description = "Total number of pages", example = "2")
    val totalPages: Int,
    @Schema(description = "Total number of elements", example = "10")
    val totalElements: Int,
    @Schema(description = "List of elements")
    val items: List<T>,
)
