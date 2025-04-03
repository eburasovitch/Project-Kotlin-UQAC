package com.example.todocontextuelapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todocontextuelapp.ui.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onBack: () -> Unit,
    locationViewModel: LocationViewModel
) {
    val defaultLocation = LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 1f)
    }

    var markerPosition by remember { mutableStateOf<LatLng?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerPosition = latLng
            }
        ) {
            markerPosition?.let { pos ->
                Marker(
                    state = MarkerState(position = pos),
                    title = "Selected Location"
                )
            }
        }
        FloatingActionButton(
            onClick = {
                markerPosition?.let { pos ->
                    locationViewModel.setLocation(pos.latitude, pos.longitude)
                }
                onBack()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Confirm Location")
        }
    }
}
