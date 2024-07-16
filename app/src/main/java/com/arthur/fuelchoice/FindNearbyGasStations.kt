package com.arthur.fuelchoice

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.arthur.fuelchoice.ui.theme.darkGray
import kotlinx.coroutines.launch
import org.json.JSONObject

data class FuelStation(
    val name: String,
    val address: String,
    val image: String?,
    val latitude: Double,
    val longitude: Double
)

fun findNearbyFuelStations(
    context: Context,
    latitude: String,
    longitude: String,
    callback: (List<FuelStation>) -> Unit
) {
    val radius = 5000
    val type = "gas_station"
    val placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
            "?location=$latitude,$longitude" +
            "&radius=$radius" +
            "&types=$type" +
            "&key=${BuildConfig.MAPS_API_KEY}"

    val requestQueue = Volley.newRequestQueue(context)
    val request = StringRequest(Request.Method.GET, placesUrl, { result ->
        val fuelStations = parseFuelStations(result)
        callback(fuelStations)
    }, { error ->
        Log.d("Error", error.message.toString())
        callback(emptyList())
    })
    requestQueue.add(request)
}

fun parseFuelStations(response: String): List<FuelStation> {
    val fuelStations = mutableListOf<FuelStation>()
    val jsonObject = JSONObject(response)
    val results = jsonObject.getJSONArray("results")

    for (i in 0 until results.length()) {
        val result = results.getJSONObject(i)
        val name = result.getString("name")
        val address = result.getString("vicinity")
        val image = if (result.has("photos") && result.getJSONArray("photos").length() > 0) {
            val photo = result.getJSONArray("photos").getJSONObject(0)
            val photoReference = photo.getString("photo_reference")
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=${BuildConfig.MAPS_API_KEY}"
        } else {
            null
        }
        val location = result.getJSONObject("geometry").getJSONObject("location")
        val latitude = location.getDouble("lat")
        val longitude = location.getDouble("lng")
        val fuelStation = FuelStation(name, address, image, latitude, longitude)
        fuelStations.add(fuelStation)
    }
    return fuelStations
}

@Composable
fun FuelStationList(stations: List<FuelStation>) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
    ) {
        for (station in stations) {
            FuelStationItem(station = station)
        }
    }
}

@Composable
fun FuelStationItem(station: FuelStation) {
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

