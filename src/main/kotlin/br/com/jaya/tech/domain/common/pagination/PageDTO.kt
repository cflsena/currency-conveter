package br.com.jaya.tech.domain.common.pagination

class PageDTO<T>(
    val pageNumber: Int,
    val pageSize: Int,
    val numberOfElements: Int,
    val totalPages: Int,
    val totalElements: Int,
    val items: List<T>,
)
