package br.com.jaya.tech.infrastructure.transaction.client

import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import java.math.BigDecimal

data class RatesResponse(
    val base: String,
    val rates: Map<String, BigDecimal>,
)

@FeignClient(
    name = "exchangeRatesClient",
    url = "\${exchange.rates.url:http://change-me:8080}",
    configuration = [AuthenticationConfig::class],
)
fun interface ExchangeRatesClient {
    @GetMapping("/latest")
    fun getRates(): RatesResponse
}

class AuthenticationConfig {
    @Value("\${exchange.rates.key}")
    lateinit var accessKey: String

    @Value("\${exchange.rates.base}")
    lateinit var baseCurrency: String

    @Bean
    fun interceptor(): RequestInterceptor =
        RequestInterceptor {
            it.query("access_key", accessKey)
            it.query("base", baseCurrency)
        }
}
