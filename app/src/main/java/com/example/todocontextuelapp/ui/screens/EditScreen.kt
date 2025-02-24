package com.example.todocontextuelapp.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    var amPm by remember { mutableStateOf(if (routine.hour < 12) "AM" else "PM") }
    var frequency by remember { mutableStateOf(routine.frequency) }

    val frequencies = listOf("Just for this time", "Every day", "Once a week")

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            ) {
                Text(
                    text = "Edit your Task",
                    color = Color.Black,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name of the task") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFB0B0B0), shape = RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFB0B0B0), shape = RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Select a date",
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFB0B0B0), shape = RoundedCornerShape(8.dp))
                        .clickable { showDatePicker(context) { selectedDate -> date = selectedDate } }
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (date.isEmpty()) "MM/DD/YYYY" else date,
                        color = if (date.isEmpty()) Color.Gray else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pick an hour",
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFB0B0B0), shape = RoundedCornerShape(8.dp))
                        .clickable { showTimePicker(context) { selectedTime ->
                            hour = selectedTime
                            amPm = if (hour < 12) "AM" else "PM"
                        } }
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (hour == 0) "HH:MM AM/PM" else "$hour $amPm",
                        color = if (hour == 0) Color.Gray else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(frequency)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
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
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val updated = routine.copy(
                            name = name,
                            description = description,
                            date = date,
                            hour = hour,
                            amPm = amPm,
                            frequency = frequency
                        )
                        onUpdateRoutine(updated)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save changes")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
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