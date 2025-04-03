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
import com.example.todocontextuelapp.ui.utils.showDatePicker
import com.example.todocontextuelapp.ui.utils.showTimePicker
import com.example.todocontextuelapp.utils.TimeNotificationHelper

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
    var amPm by remember { mutableStateOf(routine.amPm) }
    var frequency by remember { mutableStateOf(routine.frequency) }
    var category by remember { mutableStateOf(routine.category) }
    var priority by remember { mutableStateOf(routine.priority) }

    val frequencies = listOf("Just for this time", "Every day", "Once a week")
    val categories = listOf("General", "Work", "Leisure", "Health")
    val priorities = listOf("Low", "Medium", "High")

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
                        unfocusedIndicatorColor = Color.Transparent
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
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sélection de la date
                Text(text = "Select a date", fontWeight = FontWeight.Bold)
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

                // Sélection de l'heure
                Text(text = "Pick an hour", fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFB0B0B0), shape = RoundedCornerShape(8.dp))
                        .clickable {
                            showTimePicker(context) { selectedTime ->
                                hour = selectedTime
                                amPm = if (hour < 12) "AM" else "PM"
                            }
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (hour == 0) "HH:MM AM/PM" else "$hour $amPm",
                        color = if (hour == 0) Color.Gray else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fréquence
                var freqExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { freqExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(frequency)
                    }
                    DropdownMenu(
                        expanded = freqExpanded,
                        onDismissRequest = { freqExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        frequencies.forEach { item ->
                            DropdownMenuItem(onClick = {
                                frequency = item
                                freqExpanded = false
                            }) {
                                Text(item)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Catégorie
                Text(text = "Category", fontWeight = FontWeight.Bold)
                var catExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { catExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray, contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(category)
                    }
                    DropdownMenu(
                        expanded = catExpanded,
                        onDismissRequest = { catExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(onClick = {
                                category = cat
                                catExpanded = false
                            }) {
                                Text(cat)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Priorité
                Text(text = "Priority", fontWeight = FontWeight.Bold)
                var priExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { priExpanded = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray, contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(priority)
                    }
                    DropdownMenu(
                        expanded = priExpanded,
                        onDismissRequest = { priExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        priorities.forEach { pri ->
                            DropdownMenuItem(onClick = {
                                priority = pri
                                priExpanded = false
                            }) {
                                Text(pri)
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val updated = routine.copy(
                            name = name,
                            description = description,
                            date = date,
                            hour = hour,
                            amPm = amPm,
                            frequency = frequency,
                            category = category,
                            priority = priority
                        )
                        onUpdateRoutine(updated)

                        // Re-programmer la notification horaire
                        val helper = TimeNotificationHelper(context)
                        helper.scheduleRoutineNotification(updated)
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
