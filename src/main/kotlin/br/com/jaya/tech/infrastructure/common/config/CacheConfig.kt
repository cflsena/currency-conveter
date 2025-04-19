package br.com.jaya.tech.infrastructure.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class CacheConfig {
    companion object {
        private const val CACHE_PREFIX = "currency_conversion"
    }

    @Bean
    fun redisConnectionFactory(properties: RedisProperties): LettuceConnectionFactory {
        val masterConfig = RedisStaticMasterReplicaConfiguration(properties.host, properties.port!!)
        return LettuceConnectionFactory(masterConfig)
    }

    @Bean
    fun cacheConfiguration(): RedisCacheConfiguration =
        RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .prefixCacheNameWith(CACHE_PREFIX)
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))

    @Bean
    fun redisTemplate(properties: RedisProperties): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        val stringSerializer = StringRedisSerializer()
        template.connectionFactory = redisConnectionFactory(properties)
        template.keySerializer = stringSerializer
        template.valueSerializer = stringSerializer
        template.hashKeySerializer = stringSerializer
        template.hashValueSerializer = stringSerializer
        template.afterPropertiesSet()
        return template
    }
}
