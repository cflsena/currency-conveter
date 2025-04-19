package br.com.jaya.tech.infrastructure.common.cache

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

class RedisTestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        @Container
        val redisContainer: RedisContainer =
            RedisContainer(DockerImageName.parse("redis:5.0.3-alpine"))
                .withExposedPorts(6379)
    }

    override fun initialize(ctx: ConfigurableApplicationContext) {
        if (!redisContainer.isRunning) {
            redisContainer.start()
        }
        TestPropertyValues
            .of(
                "redis.host=${redisContainer.host}",
                "redis.port=${redisContainer.getMappedPort(6379)}",
            ).applyTo(ctx.environment)
    }
}
