package com.arthur.gascalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arthur.gascalculator.ui.theme.GasCalculatorTheme
import com.arthur.gascalculator.ui.theme.blackBackGround
import com.arthur.gascalculator.ui.theme.darkGray
import com.arthur.gascalculator.ui.theme.lightGray
import com.arthur.gascalculator.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GasCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
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
    Column(
        Modifier
            .background(blackBackGround)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Álcool ou Gasolina?",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF8C8C8C),
                fontSize = 24.sp
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
    var gasValue by remember {
        mutableStateOf("")
    }
    var alcoholValue by remember {
        mutableStateOf("")
    }
    if (alcoholValue.isBlank()) {
        askForInputText = "Insira o preço do litro de álcool:"
    } else if (gasValue.isBlank()) {
        askForInputText = "Insira o preço do litro de gasolina:"
    }
    if (alcoholValue.isBlank() || gasValue.isBlank()) {
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
            Text(text = "Álcool (preço por litro)", style = MaterialTheme.typography.labelMedium)
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
        value = gasValue,
        onValueChange = {
            gasValue = it
        },
        prefix = {
            Text(text = "R$ ", color = Color.White)
        },
        label = {
            Text(text = "Gasolina (preço por litro)", style = MaterialTheme.typography.labelMedium)
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
            result = AlcoholOrGasCalculator().simpleCalculate(alcoholValue, gasValue)
        },
        enabled = (alcoholValue.isNotBlank() && gasValue.isNotBlank()),
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
    var gasDistance by remember {
        mutableStateOf("")
    }
    var gasValue by remember {
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
    var gasPerfomance by remember {
        mutableStateOf("")
    }
    var text by remember {
        mutableStateOf("")
    }
    if (alcoholValue.isBlank()) {
        askForInputText = "Informe o preço do litro de álcool:"
    } else if (alcoholDistance.isBlank()) {
        askForInputText = "Informe os km rodados por litro de álcool:"
    } else if (gasValue.isBlank()) {
        askForInputText = "Informe o preço do litro de gasolina:"
    } else if (gasDistance.isBlank()) {
        askForInputText = "Informe os km rodados por litro de gasolina:"
    }
    if (alcoholValue.isBlank() ||
        gasValue.isBlank() ||
        gasDistance.isBlank() ||
        alcoholDistance.isBlank()) {
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
            Text(text = "Preço do litro de álcool", style = MaterialTheme.typography.labelMedium)
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
        value = gasValue,
        onValueChange = {
            gasValue = it
        },
        prefix = {
            Text(text = "R$ ", color = Color.White)
        },
        label = {
            Text(text = "Preço do litro de gasolina", style = MaterialTheme.typography.labelMedium)
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
        value = gasDistance,
        onValueChange = {
            gasDistance = it
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
            result = AlcoholOrGasCalculator().specificCalculate(
                alcoholValue, alcoholDistance, gasValue, gasDistance
            )[0]
            alcoholPerfomance = AlcoholOrGasCalculator().specificCalculate(
                alcoholValue, alcoholDistance, gasValue, gasDistance
            )[1]
            gasPerfomance = AlcoholOrGasCalculator().specificCalculate(
                alcoholValue, alcoholDistance, gasValue, gasDistance
            )[2]
        },
        enabled = (alcoholValue.isNotBlank() &&
                gasValue.isNotBlank() &&
                alcoholDistance.isNotBlank() &&
                gasDistance.isNotBlank()),
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
                alcoholPerfomance.toDouble(), gasPerfomance.toDouble()
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

@Preview
@Composable
fun AppPreview() {
    GasCalculatorTheme {
        App()
    }
}