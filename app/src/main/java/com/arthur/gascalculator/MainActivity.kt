package com.arthur.gascalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arthur.gascalculator.ui.theme.GasCalculatorTheme

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
    val green = Color(0xFFAFC170)
    val darkGray = Color(0xFF222222)
    val lightGray = Color(0xFF787878)
    val backGround = Color(0xFF0A0A0A)
    var gasValue by remember {
        mutableStateOf("")
    }
    var alcoholValue by remember {
        mutableStateOf("")
    }
    var result by remember {
        mutableStateOf("")
    }
    var auxText by remember {
        mutableStateOf("Por favor, insira o preço do litro:")
    }
    var isResultVisible by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .background(backGround)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                auxText,
                style = TextStyle(
                    color = Color(0xFF8C8C8C),
                    fontSize = 24.sp
                )
            )
            if (isResultVisible) {
                Text(
                    result,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            TextField(
                value = alcoholValue,
                onValueChange = {
                    alcoholValue = it
                },
                label = {
                    Text(text = "Álcool")
                },
                placeholder = {
                    Text(text = "Exemplo: 3,4")
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
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
                    Text(text = "Gasolina")
                },
                placeholder = {
                    Text(text = "Exemplo: 5,4")
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
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
                    if (alcoholValue.isNotBlank() && gasValue.isNotBlank()) {
                        result = AlcoholOrGasCalculator().genericCalculate(alcoholValue, gasValue)
                        auxText = "Álcool ou Gasolina?"
                        isResultVisible = result != ""
                    }
                },
                colors = ButtonDefaults.buttonColors(green),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(290.dp)
            )
            {
                Text(
                    text = "Calcular", style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
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