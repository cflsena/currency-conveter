package br.com.jaya.tech

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EntityScan(basePackages = ["br.com.jaya.tech.*"])
@ComponentScan(basePackages = ["br.com.jaya.tech"])
@EnableFeignClients(basePackages = ["br.com.jaya.tech"])
@ImportAutoConfiguration(FeignAutoConfiguration::class)
class CurrencyConverterApplication

fun main(args: Array<String>) {
	runApplication<CurrencyConverterApplication>(*args)
}
