package com.example.todocontextuelapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todocontextuelapp.data.Routine
import com.example.todocontextuelapp.ui.screens.CreateScreen
import com.example.todocontextuelapp.ui.screens.EditScreen
import com.example.todocontextuelapp.ui.screens.HomeScreen
import com.example.todocontextuelapp.MapScreen
import com.example.todocontextuelapp.ui.viewmodel.LocationViewModel
import com.example.todocontextuelapp.ui.viewmodel.RoutineViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: RoutineViewModel = viewModel()
) {
    val routineList by viewModel.allRoutines.collectAsState(initial = emptyList())

    // NavHost scopé à "main" pour partager le LocationViewModel
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        route = "main"
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                routines = routineList,
                onCreateClicked = { navController.navigate(Screen.Create.route) },
                onEditClicked = { routine ->
                    navController.navigate(Screen.Edit.createRoute(routine.id ?: 0))
                },
                onDeleteClicked = { routine ->
                    viewModel.deleteRoutine(routine)
                },
                onToggleCompletion = { id, isCompleted ->
                    viewModel.getRoutineById(id) { routine ->
                        routine?.let {
                            viewModel.updateRoutine(it.copy(completed = isCompleted))
                        }
                    }
                }
            )
        }
        composable(Screen.Create.route) {
            // On récupère le LocationViewModel parent
            val parentEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry("main")
            }
            val locationViewModel = viewModel<LocationViewModel>(parentEntry)

            CreateScreen(
                navController = navController,
                routineViewModel = viewModel,
                locationViewModel = locationViewModel,
                onCancel = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("routineId") { type = NavType.IntType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getInt("routineId")
            val routineState = remember { mutableStateOf<Routine?>(null) }

            LaunchedEffect(routineId) {
                if (routineId != null) {
                    viewModel.getRoutineById(routineId) { routine ->
                        routineState.value = routine
                    }
                }
            }

            val routineToEdit = routineState.value
            if (routineToEdit != null) {
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        composable(Screen.Map.route) {
            // MapScreen pour sélectionner un lieu, si on souhaite activer le geofence
            val parentEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry("main")
            }
            val locationViewModel = viewModel<LocationViewModel>(parentEntry)

            MapScreen(
                onBack = { navController.popBackStack() },
                locationViewModel = locationViewModel
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Create : Screen("create")
    object Edit : Screen("edit/{routineId}") {
        fun createRoute(routineId: Int) = "edit/$routineId"
    }
    object Map : Screen("map")
}
