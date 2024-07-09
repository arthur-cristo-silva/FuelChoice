package com.arthur.gascalculator

import java.text.NumberFormat
import java.util.Locale

class AlcoholOrGasCalculator {
    fun genericCalculate(alcoholValue: String, gasValue: String): String {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        var result: String
        try {
            val alcohol = format.parse(alcoholValue)
            val gas = format.parse(gasValue)
            val formula = (
                    alcohol?.toDouble() ?: alcoholValue.toDouble()) /
                    (gas?.toDouble() ?: gasValue.toDouble()) < 0.7
            result = if (!formula) {
                "Gasolina"
            } else if (formula) {
                "Álcool"
            } else {
                "Dados Inválidos"
            }
        } catch (e: NumberFormatException) {
            result = "Dados Inválidos"
        }
        return result
    }
}