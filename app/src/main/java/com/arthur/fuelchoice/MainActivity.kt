package com.arthur.fuelchoice

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arthur.fuelchoice.ui.theme.FuelChoiceTheme
import com.arthur.fuelchoice.ui.theme.blackBackGround
import com.arthur.fuelchoice.ui.theme.darkGray
import com.arthur.fuelchoice.ui.theme.lightGray
import com.arthur.fuelchoice.ui.theme.lightGreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            FuelChoiceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = this
                    App(context)
                }
            }
        }
    }

    @Composable
    fun App(context: Context) {
        var result by remember {
            mutableStateOf("")
        }
        var isSimpleFormula by remember {
            mutableStateOf(true)
        }
        var showGasStations by remember {
            mutableStateOf(false)
        }
        Column(
            Modifier
                .background(blackBackGround)
                .fillMaxSize()
                .safeDrawingPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!showGasStations) {
                    Text(
                        text = if (isSimpleFormula) {
                            "Cálculo Simples"
                        } else {
                            "Cálculo Específico"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF8C8C8C),
                        fontSize = 28.sp
                    )
                    if (result != "") {
                        Text(
                            result,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White, fontSize = 48.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .width(275.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { isSimpleFormula = true },
                            enabled = !isSimpleFormula,
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = darkGray,
                                disabledContentColor = lightGray,
                                containerColor = lightGreen,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .width(125.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Cálculo Simples",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                maxLines = 2,
                                textAlign = TextAlign.Center,
                            )
                        }
                        Button(
                            onClick = {
                                isSimpleFormula = false
                            },
                            enabled = isSimpleFormula,
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = darkGray,
                                disabledContentColor = lightGray,
                                containerColor = lightGreen,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .width(125.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Cálculo Específico",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                maxLines = 2,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    result = if (isSimpleFormula) {
                        simpleFormulaView()
                    } else {
                        specificFormulaView()
                    }
                    Button(
                        onClick = {
                            showGasStations = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightGreen,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.width(275.dp)
                    ) {
                        Text(
                            text = "Mostrar Postos de\nGasolina Próximos",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showGasStations = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightGreen,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.width(275.dp)
                    ) {
                        Text(
                            text = "Esconder Postos de\nGasolina Próximos",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                    FuelStations(context)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }


    @Composable
    fun FuelStations(context: Context) {
        val (latitude, longitude) = locationComponent()
        val fuelStations = remember { mutableStateOf(emptyList<FuelStation>()) }

        LaunchedEffect(latitude, longitude) {
            if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
                findNearbyFuelStations(context, latitude, longitude) { stations ->
                    fuelStations.value = stations
                }
            }
        }

        FuelStationList(fuelStations.value)
    }

    @Composable
    fun simpleFormulaView(): String {
        val keyboardController = LocalSoftwareKeyboardController.current
        var result by remember {
            mutableStateOf("")
        }
        var askForInputText by remember {
            mutableStateOf("Por favor, insira o preço do litro de álcool:")
        }
        var gasolineValue by remember {
            mutableStateOf("")
        }
        var alcoholValue by remember {
            mutableStateOf("")
        }
        if (alcoholValue.isBlank()) {
            askForInputText = "Insira o preço do litro de álcool:"
        } else if (gasolineValue.isBlank()) {
            askForInputText = "Insira o preço do litro de gasolina:"
        }
        if (alcoholValue.isBlank() || gasolineValue.isBlank()) {
            Text(
                text = askForInputText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF8C8C8C),
                fontSize = 16.sp
            )
        }
        TextField(
            value = alcoholValue,
            onValueChange = {
                alcoholValue = it
            },
            prefix = {
                Text(text = "R$ ", color = Color.White)
            },
            label = {
                Text(
                    text = "Álcool (preço por litro)",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkGray,
                unfocusedContainerColor = darkGray,
                disabledContainerColor = darkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = lightGray,
                unfocusedLabelColor = lightGray,
                focusedPlaceholderColor = lightGray,
                unfocusedPlaceholderColor = lightGray,
            )
        )
        TextField(
            value = gasolineValue,
            onValueChange = {
                gasolineValue = it
            },
            prefix = {
                Text(text = "R$ ", color = Color.White)
            },
            label = {
                Text(
                    text = "Gasolina (preço por litro)",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkGray,
                unfocusedContainerColor = darkGray,
                disabledContainerColor = darkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = lightGray,
                unfocusedLabelColor = lightGray,
                focusedPlaceholderColor = lightGray,
                unfocusedPlaceholderColor = lightGray,
            )
        )
        Button(
            onClick = {
                keyboardController?.hide()
                result =
                    AlcoholOrGasolineCalculator().simpleCalculate(alcoholValue, gasolineValue)
            },
            enabled = (alcoholValue.isNotBlank() && gasolineValue.isNotBlank()),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = darkGray,
                disabledContentColor = lightGray,
                containerColor = lightGreen,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(275.dp)
        )
        {
            Text(
                text = "Calcular", style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp
            )
        }
        if (result != "") {
            Box(
                modifier = Modifier
                    .width(275.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(darkGray)
            ) {
                Text(
                    text = "Como o cálculo é feito?\n\n" +
                            "O valor do litro do álcool é dividido pelo da gasolina.\n\n" +
                            "Quando o resultado é menor que 0,7, a recomendação é abastecer com álcool. Se maior, a recomendação é escolher a gasolina.",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(color = Color.White),
                    modifier = Modifier
                        .padding(12.dp)
                )
            }
        }
        return result
    }

    @Composable
    fun specificFormulaView(): String {
        val keyboardController = LocalSoftwareKeyboardController.current
        var result by remember {
            mutableStateOf("")
        }
        var askForInputText by remember {
            mutableStateOf("Por favor, insira o preço do litro de álcool:")
        }
        var gasolineDistance by remember {
            mutableStateOf("")
        }
        var gasolineValue by remember {
            mutableStateOf("")
        }
        var alcoholDistance by remember {
            mutableStateOf("")
        }
        var alcoholValue by remember {
            mutableStateOf("")
        }
        var alcoholPerfomance by remember {
            mutableStateOf("")
        }
        var gasolinePerfomance by remember {
            mutableStateOf("")
        }
        var text by remember {
            mutableStateOf("")
        }
        if (alcoholValue.isBlank()) {
            askForInputText = "Informe o preço do litro de álcool:"
        } else if (alcoholDistance.isBlank()) {
            askForInputText = "Informe os km rodados por litro de álcool:"
        } else if (gasolineValue.isBlank()) {
            askForInputText = "Informe o preço do litro de gasolina:"
        } else if (gasolineDistance.isBlank()) {
            askForInputText = "Informe os km rodados por litro de gasolina:"
        }
        if (alcoholValue.isBlank() ||
            gasolineValue.isBlank() ||
            gasolineDistance.isBlank() ||
            alcoholDistance.isBlank()
        ) {
            Text(
                text = askForInputText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF8C8C8C),
                fontSize = 16.sp
            )
        }
        TextField(
            value = alcoholValue,
            onValueChange = {
                alcoholValue = it
            },
            prefix = {
                Text(text = "R$ ", color = Color.White)
            },
            label = {
                Text(
                    text = "Preço do litro de álcool",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkGray,
                unfocusedContainerColor = darkGray,
                disabledContainerColor = darkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = lightGray,
                unfocusedLabelColor = lightGray,
                focusedPlaceholderColor = lightGray,
                unfocusedPlaceholderColor = lightGray,
            )
        )
        TextField(
            value = alcoholDistance,
            onValueChange = {
                alcoholDistance = it
            },
            label = {
                Text(
                    text = "Km por litro de álcool",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkGray,
                unfocusedContainerColor = darkGray,
                disabledContainerColor = darkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = lightGray,
                unfocusedLabelColor = lightGray,
                focusedPlaceholderColor = lightGray,
                unfocusedPlaceholderColor = lightGray,
            )
        )
        TextField(
            value = gasolineValue,
            onValueChange = {
                gasolineValue = it
            },
            prefix = {
                Text(text = "R$ ", color = Color.White)
            },
            label = {
                Text(
                    text = "Preço do litro de gasolina",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkGray,
                unfocusedContainerColor = darkGray,
                disabledContainerColor = darkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = lightGray,
                unfocusedLabelColor = lightGray,
                focusedPlaceholderColor = lightGray,
                unfocusedPlaceholderColor = lightGray,
            )
        )
        TextField(
            value = gasolineDistance,
            onValueChange = {
                gasolineDistance = it
            },
            label = {
                Text(
                    text = "Km por litro de gasolina",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = darkGray,
                unfocusedContainerColor = darkGray,
                disabledContainerColor = darkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = lightGray,
                unfocusedLabelColor = lightGray,
                focusedPlaceholderColor = lightGray,
                unfocusedPlaceholderColor = lightGray,
            )
        )
        Button(
            onClick = {
                keyboardController?.hide()
                result = AlcoholOrGasolineCalculator().specificCalculate(
                    alcoholValue, alcoholDistance, gasolineValue, gasolineDistance
                )[0]
                alcoholPerfomance = AlcoholOrGasolineCalculator().specificCalculate(
                    alcoholValue, alcoholDistance, gasolineValue, gasolineDistance
                )[1]
                gasolinePerfomance = AlcoholOrGasolineCalculator().specificCalculate(
                    alcoholValue, alcoholDistance, gasolineValue, gasolineDistance
                )[2]
            },
            enabled = (alcoholValue.isNotBlank() &&
                    gasolineValue.isNotBlank() &&
                    alcoholDistance.isNotBlank() &&
                    gasolineDistance.isNotBlank()),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = darkGray,
                disabledContentColor = lightGray,
                containerColor = lightGreen,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(275.dp)
        )
        {
            Text(
                text = "Calcular", style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp
            )
        }
        if (result != "") {
            Box(
                modifier = Modifier
                    .width(275.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(darkGray)
            ) {
                text = String.format(
                    "Por quê ${result.lowercase()} vale mais a pena?\n\n" +
                            "Álcool: R$%.2f por km.\n" +
                            "Gasolina: R$%.2f por km.",
                    alcoholPerfomance.toDouble(), gasolinePerfomance.toDouble()
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                        .copy(color = Color.White),
                    modifier = Modifier
                        .padding(12.dp)
                )
            }
        }
        return result
    }

    @Composable
    @SuppressLint("MissingPermission")
    fun locationComponent(): List<String> {
        var latitude by remember { mutableStateOf("") }
        var longitude by remember { mutableStateOf("") }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                latitude = loc?.latitude?.toString().orEmpty()
                longitude = loc?.longitude?.toString().orEmpty()
            }
        }
        return listOf(latitude, longitude)
    }

    @Preview
    @Composable
    fun AppPreview() {
        FuelChoiceTheme {
            App(this)
        }
    }
}
