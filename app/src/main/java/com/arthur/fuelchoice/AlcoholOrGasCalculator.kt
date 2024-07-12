package com.arthur.fuelchoice

import java.text.NumberFormat
import java.util.Locale

class AlcoholOrGasolineCalculator {
    fun simpleCalculate(alcoholValue: String, gasolineValue: String): String {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        var result: String
        try {
            val alcohol = format.parse(alcoholValue)?.toDouble() ?: alcoholValue.toDouble()
            val gasoline = format.parse(gasolineValue)?.toDouble() ?: gasolineValue.toDouble()
            result = if (alcohol / gasoline < 0.7) {
                "Álcool"
            } else {
                "Gasolina"
            }
        } catch (e: NumberFormatException) {
            result = "Dados Inválidos"
        }
        return result
    }

    fun specificCalculate(
        alcoholValue: String,
        alcoholDistance: String,
        gasolineValue: String,
        gasolineDistance: String
    ): List<String> {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        var result: String
        var alcoholPerformance = 0.0
        var gasolinePerformance = 0.0
        try {
            val alcoholPrice = format.parse(alcoholValue)?.toDouble() ?: alcoholValue.toDouble()
            val alcoholDis = format.parse(alcoholDistance)?.toDouble() ?: alcoholDistance.toDouble()
            val gasolinePrice = format.parse(gasolineValue)?.toDouble() ?: gasolineValue.toDouble()
            val gasolineDis = format.parse(gasolineDistance)?.toDouble() ?: gasolineDistance.toDouble()
            alcoholPerformance = alcoholPrice / alcoholDis
            gasolinePerformance = gasolinePrice / gasolineDis
            result = when {
                alcoholPerformance < gasolinePerformance -> "Álcool"
                alcoholPerformance > gasolinePerformance -> "Gasolina"
                else -> "Idem"
            }
        } catch (e: NumberFormatException) {
            result = "Dados Inválidos"
        }
        return listOf(result, alcoholPerformance.toString(), gasolinePerformance.toString())
    }
}