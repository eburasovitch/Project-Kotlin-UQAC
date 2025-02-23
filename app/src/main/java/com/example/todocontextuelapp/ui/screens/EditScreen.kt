package com.example.todocontextuelapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todocontextuelapp.data.Routine

@Composable
fun EditScreen(
    routine: Routine,
    onUpdateRoutine: (Routine) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(routine.name) }
    var description by remember { mutableStateOf(routine.description) }
    var date by remember { mutableStateOf(routine.date) }
    var hour by remember { mutableStateOf(routine.hour) }
    var frequency by remember { mutableStateOf(routine.frequency) }
    // var amPm by remember { mutableStateOf(routine.amPm) } // si nécessaire

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Task") })
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

            Button(onClick = {
                showDatePicker(context) { selectedDate -> date = selectedDate }
            }) {
                Text(if (date.isEmpty()) "Select a date" else "Date: $date")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                showTimePicker(context) { selectedTime -> hour = selectedTime }
            }) {
                Text("Hour: $hour")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = frequency,
                onValueChange = { frequency = it },
                label = { Text("Frequency") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    val updated = routine.copy(
                        name = name,
                        description = description,
                        date = date,
                        hour = hour,
                        frequency = frequency
                    )
                    onUpdateRoutine(updated)
                }) {
                    Text("Save changes")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    com.example.todocontextuelapp.ui.utils.showDatePicker(context, onDateSelected)
}

fun showTimePicker(context: Context, onTimeSelected: (Int) -> Unit) {
    com.example.todocontextuelapp.ui.utils.showTimePicker(context, onTimeSelected)
}
