package br.com.jaya.tech.application.transaction.list

import br.com.jaya.tech.application.UseCaseDefault
import br.com.jaya.tech.domain.common.pagination.PageDTO

interface ListCurrencyConversionsUseCase :
    UseCaseDefault<CurrencyConversionsFilterInput, PageDTO<CurrencyConversionsOutput>> {
}