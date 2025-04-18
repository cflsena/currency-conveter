package br.com.jaya.tech.infrastructure.common.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun jacksonCustomizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { builder ->
            builder.apply {
                modulesToInstall(
                    KotlinModule::class.java,
                    Jdk8Module::class.java,
                    JavaTimeModule::class.java
                )
                featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                serializationInclusion(JsonInclude.Include.NON_NULL)
            }
        }

}