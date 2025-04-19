package br.com.jaya.tech.infrastructure.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "redis")
class RedisProperties {
    lateinit var host: String
    var port: Int? = 0
}
