package br.com.jaya.tech.infrastructure.transaction.service

import br.com.jaya.tech.application.service.ConversionRateService
import br.com.jaya.tech.infrastructure.transaction.client.ExchangeRatesClient
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DefaultConversionRateService(
    private val client: ExchangeRatesClient,
) : ConversionRateService {
    override fun getRates(): Map<String, BigDecimal> = client.getRates().rates
}
