package com.arthur.fuelchoice

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.core.app.ActivityCompat
import com.arthur.fuelchoice.ui.theme.FuelChoiceTheme
import com.arthur.fuelchoice.ui.theme.blackBackGround
import com.arthur.fuelchoice.ui.theme.darkGray
import com.arthur.fuelchoice.ui.theme.lightGray
import com.arthur.fuelchoice.ui.theme.lightGreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                44
            )
            false
        } else {
            true
        }
    }

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
                    App()
                    isLocationPermissionGranted()
                }
            }
        }
    }

    @Composable
    fun App() {
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
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!showGasStations) {
                    Text(
                        text = "FuelChoice",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    )
                    if (result != "") {

                        Text(
                            text = "Vale mais a pena abastecer com",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF8C8C8C),
                            fontSize = 22.sp
                        )
                        Text(
                            result,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White, fontSize = 48.sp
                        )
                    } else {
                        Text(
                            text = "Cálculo " +
                                    if (isSimpleFormula) {
                                        "Simples"
                                    } else {
                                        "Específico"
                                    },
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF8C8C8C),
                            fontSize = 22.sp
                        )
                    }
                    Button(
                        onClick = { isSimpleFormula = !isSimpleFormula },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightGreen,
                            contentColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(275.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Mudar para\n" +
                                    if (isSimpleFormula) {
                                        "Cálculo Específico"
                                    } else {
                                        "Cálculo Simples"
                                    },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                        )
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
                        enabled = isLocationPermissionGranted(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightGreen,
                            contentColor = Color.Black,
                            disabledContainerColor = lightGreen,
                            disabledContentColor = Color.Black
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
                            if (isLocationPermissionGranted()) {
                                showGasStations = false
                            }
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
                    FuelStations(LocalContext.current)
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
                alcoholValue = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
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
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
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
                gasolineValue = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
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
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
            ),
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
        var text by remember {
            mutableStateOf("")
        }
        var currency by remember {
            mutableStateOf("")
        }
        if (currency.isBlank()) {
            askForInputText = "Informe o valor do abastecimento em R$:"
        } else if (alcoholValue.isBlank()) {
            askForInputText = "Informe o preço do litro de álcool:"
        } else if (alcoholDistance.isBlank()) {
            askForInputText = "Informe os km rodados por litro de álcool:"
        } else if (gasolineValue.isBlank()) {
            askForInputText = "Informe o preço do litro de gasolina:"
        } else if (gasolineDistance.isBlank()) {
            askForInputText = "Informe os km rodados por litro de gasolina:"
        }
        if (currency.isBlank() ||
            alcoholValue.isBlank() ||
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
            value = currency,
            onValueChange = {
                currency = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
            },
            prefix = {
                Text(text = "R$ ", color = Color.White)
            },
            label = {
                Text(
                    text = "Valor do abastecimento",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
            ),
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
            value = alcoholValue,
            onValueChange = {
                alcoholValue = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
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
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
            ),
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
                gasolineValue = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
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
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
            ),
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
                    currency,
                    alcoholValue, alcoholDistance, gasolineValue, gasolineDistance
                )[0]
                text = AlcoholOrGasolineCalculator().specificCalculate(
                    currency,
                    alcoholValue, alcoholDistance, gasolineValue, gasolineDistance
                )[1]
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
        if (isLocationPermissionGranted()) {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                val fusedLocationClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    latitude = loc?.latitude?.toString().orEmpty()
                    longitude = loc?.longitude?.toString().orEmpty()
                }
            }
        }
        return listOf(latitude, longitude)
    }

    @Preview
    @Composable
    fun AppPreview() {
        FuelChoiceTheme {
            App()
        }
    }
}
