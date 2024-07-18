package com.arthur.fuelchoice.scripts

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
        currency: String,
        alcoholValue: String,
        alcoholDistance: String,
        gasolineValue: String,
        gasolineDistance: String
    ): List<String> {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        var result: String
        var text: String
        try {
            val currencyValue =
                format.parse(currency)?.toDouble()?.div(100) ?: currency.toDouble().div(100)
            val alcoholPrice =
                format.parse(alcoholValue)?.toDouble()?.div(100) ?: alcoholValue.toDouble().div(100)
            val alcoholDis = format.parse(alcoholDistance)?.toDouble() ?: alcoholDistance.toDouble()
            val gasolinePrice =
                format.parse(gasolineValue)?.toDouble()?.div(100) ?: gasolineValue.toDouble()
                    .div(100)
            val gasolineDis =
                format.parse(gasolineDistance)?.toDouble() ?: gasolineDistance.toDouble()
            val alcoholLiters = currencyValue.div(alcoholPrice)
            val alcoholKm = alcoholLiters.times(alcoholDis)
            val gasolineLiters = currencyValue.div(gasolinePrice)
            val gasolineKm = gasolineLiters.times(gasolineDis)
            result = when {
                gasolineKm > alcoholKm -> "Gasolina"
                gasolineKm < alcoholKm -> "Álcool"
                else -> "Idem"
            }
            text = if (result != "Idem") {
                String.format(
                    Locale.getDefault(),
                    "$result vale mais a pena, porquê com R$%.2f você consegue abastecer:\n\n" +
                            "Álcool: %.1fL, o que lhe permite dirigir por %.1fkm.\n\n" +
                            "Gasolina: %.1fL, o que lhe permite dirigir por %.1fkm.", currencyValue,
                    alcoholLiters, alcoholKm,
                    gasolineLiters, gasolineKm
                )
            } else {
                String.format(
                    Locale.getDefault(),
                    "Ambos valem a pena, porquê com R$%.2f você consegue abastecer:\n\n" +
                            "Álcool: %.1fL, o que lhe permite dirigir por %.1fkm.\n\n" +
                            "Gasolina: %.1fL, o que lhe permite dirigir por %.1fkm.", currencyValue,
                    alcoholLiters, alcoholKm,
                    gasolineLiters, gasolineKm
                )
            }
        } catch (e: NumberFormatException) {
            result = "Dados Inválidos"
            text = "Dados Inválidos"
        }
        return listOf(result, text)
    }
}