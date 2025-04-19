package br.com.jaya.tech.infrastructure.common.config

import feign.okhttp.OkHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {
    @Bean
    fun client(): OkHttpClient = OkHttpClient()

    @Bean
    fun httpClientBuilder(): HttpClientBuilder = HttpClientBuilder.create()
}
