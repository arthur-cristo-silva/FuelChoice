package com.arthur.fuelchoice.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.arthur.fuelchoice.Routes
import com.arthur.fuelchoice.scripts.GasStation
import com.arthur.fuelchoice.scripts.findNearbyGasStations
import com.arthur.fuelchoice.scripts.isLocationPermissionGranted
import com.arthur.fuelchoice.ui.theme.blackBackGround
import com.arthur.fuelchoice.ui.theme.darkGray
import com.arthur.fuelchoice.ui.theme.lightGreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@Composable
@SuppressLint("MissingPermission")
fun locationComponent(): List<String> {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    val activity = LocalContext.current as Activity
    if (isLocationPermissionGranted(activity)) {
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

@Composable
fun GasStationList(stations: List<GasStation>) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .verticalScroll(rememberScrollState())
    ) {
        for (station in stations) {
            GasStationItem(station = station)
        }
    }
}

@Composable
fun GasStationItem(station: GasStation) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { /* handle result */ }
    val coroutineScope = rememberCoroutineScope()
    if (station.image != null) {
        Column(
            modifier = Modifier
                .padding(
                    16.dp
                )
                .width(275.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(darkGray)
                .clickable {
                    coroutineScope.launch {
                        val uri =
                            "geo:${station.latitude},${station.longitude}?q=${station.name},${station.address}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps")
                        launcher.launch(intent)
                    }
                }
        ) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = station.address,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(model = station.image),
                contentDescription = "Fuel Station Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun GasStationsScreen(navController: NavController) {
    val context = LocalContext.current
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
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate(Routes.mainScreen)
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
            val (latitude, longitude) = locationComponent()
            val gasStations = remember { mutableStateOf(emptyList<GasStation>()) }
            LaunchedEffect(latitude, longitude) {
                if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
                    findNearbyGasStations(context, latitude, longitude) { stations ->
                        gasStations.value = stations
                    }
                }
            }
            GasStationList(gasStations.value)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}