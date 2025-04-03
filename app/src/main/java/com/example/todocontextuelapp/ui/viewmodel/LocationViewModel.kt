package com.example.todocontextuelapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel partagé pour stocker la localisation sélectionnée.
 */
class LocationViewModel : ViewModel() {

    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude: StateFlow<Double?> get() = _latitude

    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude: StateFlow<Double?> get() = _longitude

    fun setLocation(lat: Double, lon: Double) {
        _latitude.value = lat
        _longitude.value = lon
    }

    fun resetLocation() {
        _latitude.value = null
        _longitude.value = null
    }
}
