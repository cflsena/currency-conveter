package br.com.jaya.tech.infrastructure.common.cache

import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ContextConfiguration(initializers = [RedisTestContainerInitializer::class])
annotation class RedisContainerTest
