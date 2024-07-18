package com.arthur.fuelchoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arthur.fuelchoice.screens.GasStationsScreen
import com.arthur.fuelchoice.screens.MainScreen
import com.arthur.fuelchoice.ui.theme.FuelChoiceTheme
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
                    val navControl = rememberNavController()
                    NavHost(navControl, startDestination = Routes.mainScreen,
                        builder = {
                            composable(Routes.mainScreen) {
                                MainScreen(navControl)
                            }
                            composable(Routes.gasStationsScreen) {
                                GasStationsScreen(navControl)
                            }
                        })
                }
            }
        }
    }

    @Preview
    @Composable
    fun AppPreview() {
        FuelChoiceTheme {
            MainScreen(rememberNavController())
        }
    }
}
