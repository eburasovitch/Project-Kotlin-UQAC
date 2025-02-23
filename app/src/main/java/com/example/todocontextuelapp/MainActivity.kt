package com.example.todocontextuelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.todocontextuelapp.navigation.SetupNavGraph
import com.example.todocontextuelapp.ui.theme.TodoContextuelAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    TodoContextuelAppTheme {
        SetupNavGraph(navController)
    }
}
