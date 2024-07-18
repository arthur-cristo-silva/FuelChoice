package com.arthur.fuelchoice.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arthur.fuelchoice.BuildConfig
import com.arthur.fuelchoice.Routes
import com.arthur.fuelchoice.scripts.AlcoholOrGasolineCalculator
import com.arthur.fuelchoice.scripts.CurrencyAmountInputVisualTransformation
import com.arthur.fuelchoice.scripts.LockScreenOrientation
import com.arthur.fuelchoice.scripts.isLocationPermissionGranted
import com.arthur.fuelchoice.ui.theme.blackBackGround
import com.arthur.fuelchoice.ui.theme.darkGray
import com.arthur.fuelchoice.ui.theme.lightGray
import com.arthur.fuelchoice.ui.theme.lightGreen

@Composable
fun MainScreen(navController: NavController) {
    val activity = LocalContext.current as Activity
    var result by rememberSaveable {
        mutableStateOf("")
    }
    var isSimpleFormula by remember {
        mutableStateOf(true)
    }
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
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
                    navController.navigate(Routes.gasStationsScreen)
                },
                enabled = (isLocationPermissionGranted(activity) && BuildConfig.IS_API_ENABLED),
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
        }
    }
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
    var gasolineValue by rememberSaveable {
        mutableStateOf("")
    }
    var alcoholValue by rememberSaveable {
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
                AlcoholOrGasolineCalculator().simpleCalculate(
                    alcoholValue,
                    gasolineValue
                )
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
    var gasolineDistance by rememberSaveable {
        mutableStateOf("")
    }
    var gasolineValue by rememberSaveable {
        mutableStateOf("")
    }
    var alcoholDistance by rememberSaveable {
        mutableStateOf("")
    }
    var alcoholValue by rememberSaveable {
        mutableStateOf("")
    }
    var text by remember {
        mutableStateOf("")
    }
    var currency by rememberSaveable {
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