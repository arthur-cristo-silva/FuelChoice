package com.arthur.gascalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App() {
    val keyboardController = LocalSoftwareKeyboardController.current
    var gasValue by remember {
        mutableStateOf("")
    }
    var alcoholValue by remember {
        mutableStateOf("")
    }
    var result by remember {
        mutableStateOf("")
    }
    var isResultVisible by remember {
        mutableStateOf(false)
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
            if (isResultVisible) {
                Text(
                    text = "Álcool ou Gasolina?",
                    style = MaterialTheme.typography.labelLarge
                        .copy(color = Color(0xFF8C8C8C))
                )
                Text(
                    result,
                    style = MaterialTheme.typography.titleLarge
                        .copy(color = Color.White, fontSize = 48.sp)
                )
            } else {
                Text(
                    text = "Por favor, insira o preço do litro:",
                    style = MaterialTheme.typography.labelLarge
                        .copy(
                            color = Color(0xFF8C8C8C),
                            fontSize = 18.sp
                        )
                )
            }
            TextField(
                value = alcoholValue,
                onValueChange = {
                    alcoholValue = it
                },
                label = {
                    Text(text = "Álcool", style = MaterialTheme.typography.labelMedium)
                },
                placeholder = {
                    Text(text = "Exemplo: 3,4", style = MaterialTheme.typography.labelMedium)
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
                label = {
                    Text(text = "Gasolina", style = MaterialTheme.typography.labelMedium)
                },
                placeholder = {
                    Text(text = "Exemplo: 5,4", style = MaterialTheme.typography.labelMedium)
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
                    result = if (alcoholValue.isNotBlank() && gasValue.isNotBlank()) {
                        AlcoholOrGasCalculator().genericCalculate(alcoholValue, gasValue)
                    } else {
                        ""
                    }
                    isResultVisible = result != ""
                },
                colors = ButtonDefaults.buttonColors(lightGreen),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(275.dp)
            )
            {
                Text(
                    text = "Calcular", style = MaterialTheme.typography.labelLarge
                        .copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                )
            }
            if (isResultVisible) {
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
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    GasCalculatorTheme {
        App()
    }
}