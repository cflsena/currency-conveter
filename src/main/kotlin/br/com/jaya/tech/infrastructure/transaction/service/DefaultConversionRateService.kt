package br.com.jaya.tech.infrastructure.transaction.service

import br.com.jaya.tech.application.service.ConversionRateService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DefaultConversionRateService : ConversionRateService {
    override fun getRates(): Map<String, BigDecimal> {
        TODO("Not yet implemented")
    }
}