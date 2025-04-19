package br.com.jaya.tech.infrastructure.common.database

import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ContextConfiguration(initializers = [PostgresTestContainerInitializer::class])
annotation class PostgresContainerTest
