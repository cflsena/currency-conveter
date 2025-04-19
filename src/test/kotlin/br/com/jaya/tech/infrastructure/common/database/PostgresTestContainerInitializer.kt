package br.com.jaya.tech.infrastructure.common.database

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class PostgresTestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        @Container
        val postgresContainer =
            PostgreSQLContainer<Nothing>("postgres:14-alpine").apply {
                withInitScript("sql/init-db.sql")
                withUrlParam("currentSchema", "CURRENCY_CONVERSION")
            }
    }

    override fun initialize(ctx: ConfigurableApplicationContext) {
        if (!postgresContainer.isRunning) {
            postgresContainer.start()
        }
        TestPropertyValues
            .of(
                "spring.datasource.url=${postgresContainer.jdbcUrl}",
                "spring.datasource.username=${postgresContainer.username}",
                "spring.datasource.password=${postgresContainer.password}",
            ).applyTo(ctx.environment)
    }
}
