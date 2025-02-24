package com.example.todocontextuelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.todocontextuelapp.navigation.SetupNavGraph
import com.example.todocontextuelapp.ui.theme.TodoContextuelAppTheme
import com.example.todocontextuelapp.data.FileRoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
    @Composable
    fun MyApp() {
        val navController = rememberNavController()
        val repository = FileRoutineRepository.getInstance(applicationContext)

        TodoContextuelAppTheme {
            SetupNavGraph(
                navController = navController,
                repository = repository,
                coroutineScope = applicationScope
            )
        }
    }

}

