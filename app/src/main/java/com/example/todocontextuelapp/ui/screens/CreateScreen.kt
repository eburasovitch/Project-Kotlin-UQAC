package com.example.todocontextuelapp.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todocontextuelapp.data.Routine
import com.example.todocontextuelapp.ui.viewmodel.RoutineViewModel
import java.util.*

@Composable
fun CreateScreen(
    viewModel: RoutineViewModel = viewModel(),
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf(0) }
    var amPm by remember { mutableStateOf("AM") }
    var frequency by remember { mutableStateOf("Just for this time") }

    val frequencies = listOf("Just for this time", "Every day", "Once a week")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Task") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name of the task") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sélecteur de Date
            Button(onClick = {
                showLocalDatePicker(context) { selectedDate -> date = selectedDate }
            }) {
                Text(if (date.isEmpty()) "Select a date" else "Date: $date")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sélecteur d'Heure
            Button(onClick = {
                showLocalTimePicker(context) { selectedTime ->
                    hour = selectedTime
                    amPm = if (hour < 12) "AM" else "PM"
                }
            }) {
                Text("Hour: $hour $amPm")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown pour la fréquence
            var expanded by remember { mutableStateOf(false) }
            Box {
                Button(onClick = { expanded = true }) {
                    Text(frequency)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    frequencies.forEach { item ->
                        DropdownMenuItem(onClick = {
                            frequency = item
                            expanded = false
                        }) {
                            Text(item)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    val newRoutine = Routine(
                        id = 0,
                        name = name,
                        description = description,
                        date = date,
                        hour = hour,
                        amPm = amPm,
                        frequency = frequency
                    )
                    viewModel.addRoutine(newRoutine)
                    onCancel()
                }) {
                    Text("Create the task")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        }
    }
}

/**
 * Affiche un DatePicker
 */
fun showLocalDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected("${month + 1}/$dayOfMonth/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

/**
 * Affiche un TimePicker
 */
fun showLocalTimePicker(context: Context, onTimeSelected: (Int) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hourOfDay, _ ->
            onTimeSelected(hourOfDay)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        0,
        false
    ).show()
}
