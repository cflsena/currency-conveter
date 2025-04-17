package br.com.jaya.tech.application.service

import java.math.BigDecimal

fun interface ConversionRateService {
    fun getRates() : Map<String, BigDecimal>
}