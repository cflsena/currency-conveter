package br.com.jaya.tech.domain.transaction

import java.math.BigDecimal
import java.math.RoundingMode

object ConversionRateCalculator {

    private const val DEFAULT_CONVERSION_RATE_SCALE_SIZE = 7
    private val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN

    fun calculate(originRate: BigDecimal, destinationRate: BigDecimal): BigDecimal {
        return destinationRate.divide(originRate, DEFAULT_CONVERSION_RATE_SCALE_SIZE, DEFAULT_ROUNDING_MODE)
    }

}