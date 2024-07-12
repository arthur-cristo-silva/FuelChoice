package com.arthur.gascalculator

import java.text.NumberFormat
import java.util.Locale

class AlcoholOrGasCalculator {
    fun simpleCalculate(alcoholValue: String, gasValue: String): String {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        var result: String
        try {
            val alcohol = format.parse(alcoholValue)?.toDouble() ?: alcoholValue.toDouble()
            val gas = format.parse(gasValue)?.toDouble() ?: gasValue.toDouble()
            result = if (alcohol / gas < 0.7) {
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
        gasValue: String,
        gasDistance: String
    ): List<String> {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        var result: String
        var alcoholPerformance = 0.0
        var gasPerformance = 0.0
        try {
            val alcoholPrice = format.parse(alcoholValue)?.toDouble() ?: alcoholValue.toDouble()
            val alcoholDis = format.parse(alcoholDistance)?.toDouble() ?: alcoholDistance.toDouble()
            val gasPrice = format.parse(gasValue)?.toDouble() ?: gasValue.toDouble()
            val gasDis = format.parse(gasDistance)?.toDouble() ?: gasDistance.toDouble()
            alcoholPerformance = alcoholPrice / alcoholDis
            gasPerformance = gasPrice / gasDis
            result = when {
                alcoholPerformance < gasPerformance -> "Álcool"
                alcoholPerformance > gasPerformance -> "Gasolina"
                else -> "Idem"
            }
        } catch (e: NumberFormatException) {
            result = "Dados Inválidos"
        }
        return listOf(result, alcoholPerformance.toString(), gasPerformance.toString())
    }
}