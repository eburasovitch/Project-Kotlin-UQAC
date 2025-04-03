package com.example.todocontextuelapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.todocontextuelapp.navigation.SetupNavGraph
import com.example.todocontextuelapp.ui.theme.TodoContextuelAppTheme

class MainActivity : ComponentActivity() {

    private val requiredPermissions = mutableListOf<String>().apply {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        // Gérer les résultats si besoin
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérification des permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionsToRequest = requiredPermissions.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }
            if (permissionsToRequest.isNotEmpty()) {
                permissionLauncher.launch(permissionsToRequest.toTypedArray())
            }
        }

        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        val navController = rememberNavController()
        TodoContextuelAppTheme {
            SetupNavGraph(navController = navController)
        }
    }
}
