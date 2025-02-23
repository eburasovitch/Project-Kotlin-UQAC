package com.example.todocontextuelapp.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.todocontextuelapp.data.Routine
import com.example.todocontextuelapp.ui.screens.CreateScreen
import com.example.todocontextuelapp.ui.screens.EditScreen
import com.example.todocontextuelapp.ui.screens.HomeScreen
import com.example.todocontextuelapp.ui.viewmodel.RoutineViewModel
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: RoutineViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val routineList by viewModel.allRoutines.collectAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                routines = routineList,
                onCreateClicked = {
                    navController.navigate(Screen.Create.route)
                },
                onEditClicked = { routine ->
                    Log.d("NavGraph", "Edit button clicked for routine id: ${routine.id}")
                    // Naviguer vers la route paramétrée
                    navController.navigate(Screen.Edit.createRoute(routine.id))
                },
                onDeleteClicked = { routine ->
                    viewModel.deleteRoutine(routine)
                }
            )
        }

        // Create
        composable(Screen.Create.route) {
            CreateScreen(
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // Edit
        composable(
            route = Screen.Edit.route,
            arguments = listOf(
                navArgument("routineId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getInt("routineId")
            val routineState = remember { mutableStateOf<Routine?>(null) }

            LaunchedEffect(routineId) {
                if (routineId != null) {
                    Log.d("NavGraph", "LaunchedEffect for EditScreen with routineId=$routineId")
                    viewModel.getRoutineById(routineId) { routine ->
                        routineState.value = routine
                    }
                }
            }

            val routineToEdit = routineState.value
            Log.d("MyTag", "La valeur de myVariable est : $routineToEdit")
            if (routineToEdit != null) {
                Log.d("MonTag", "One rentre dasn le if ")
                EditScreen(
                    routine = routineToEdit,
                    onUpdateRoutine = { updatedRoutine ->
                        viewModel.updateRoutine(updatedRoutine)
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                )
            } else {
                Log.d("MonTag", "on rentre dasn le else if")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Create : Screen("create")

    // Route paramétrée
    object Edit : Screen("edit/{routineId}") {
        fun createRoute(routineId: Int) = "edit/$routineId"
    }
}
