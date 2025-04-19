package br.com.jaya.tech.infrastructure.transaction.service

import br.com.jaya.tech.application.service.ConversionRateService
import br.com.jaya.tech.infrastructure.transaction.client.ExchangeRatesClient
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class DefaultConversionRateService(
    private val client: ExchangeRatesClient,
) : ConversionRateService {
    override fun getRates(): Map<String, BigDecimal> = client.getRates().rates
}

@Primary
@Service
class CachingConversionRateService(
    private val defaultService: DefaultConversionRateService,
    private val redis: StringRedisTemplate,
    private val objectMapper: ObjectMapper,
) : ConversionRateService {
    companion object {
        private const val KEY = "rates:"
        private val TTL = Duration.ofHours(8)
    }

    override fun getRates(): Map<String, BigDecimal> {
        val cachedJson = redis.opsForValue()[KEY]

        if (!cachedJson.isNullOrEmpty()) {
            return objectMapper.readValue(
                cachedJson,
                object : TypeReference<Map<String, BigDecimal>>() {},
            )
        }
        val rates = defaultService.getRates()
        val json = objectMapper.writeValueAsString(rates)
        redis.opsForValue()[KEY] = json
        redis.expire(KEY, TTL.seconds, TimeUnit.SECONDS)
        return rates
    }
}
