package com.example.todocontextuelapp.viewmodel

import com.example.todocontextuelapp.ui.viewmodel.LocationViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocationViewModelTest {

    private lateinit var locationViewModel: LocationViewModel

    @Before
    fun setUp() {
        locationViewModel = LocationViewModel()
    }

    @Test
    fun `test setLocation`() = runBlocking {
        val lat = 48.0
        val lon = -71.0
        locationViewModel.setLocation(lat, lon)
        val latValue = locationViewModel.latitude.first()
        val lonValue = locationViewModel.longitude.first()
        assertEquals(48.0, latValue)
        assertEquals(-71.0, lonValue)
    }

    @Test
    fun `test resetLocation`() = runBlocking {
        locationViewModel.setLocation(10.0, 20.0)
        locationViewModel.resetLocation()
        val latValue = locationViewModel.latitude.first()
        val lonValue = locationViewModel.longitude.first()
        assertEquals(null, latValue)
        assertEquals(null, lonValue)
    }
}
