package com.example.todocontextuelapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todocontextuelapp.data.Routine
import androidx.compose.ui.unit.sp
import com.example.todocontextuelapp.data.FileRoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    routines: List<Routine>,
    onCreateClicked: () -> Unit,
    onEditClicked: (Routine) -> Unit,
    onDeleteClicked: (Routine) -> Unit,
    repository: FileRoutineRepository,
    coroutineScope: CoroutineScope
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            ) {
                Text(
                    text = "Todo tasks",
                    color = Color.Black,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClicked, backgroundColor = Color(0xFF2C2C2C), contentColor = Color.White) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(routines) { routine ->
                    TaskCard(
                        routine = routine,
                        onEditClicked = { onEditClicked(routine) },
                        onDeleteClicked = { onDeleteClicked(routine) },
                        onCheckedChange = { isChecked ->
                            coroutineScope.launch {
                                repository.toggleRoutineCompletion(routine.id, isChecked)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    routine: Routine,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var isCompleted by remember { mutableStateOf(routine.completed) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = {
                        isCompleted = it
                        onCheckedChange(it)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50),
                        uncheckedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Set for: ${routine.hour} ${routine.amPm}",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Row {
                Button(
                    onClick = onEditClicked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(60.dp)
                        .height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.Black)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDeleteClicked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(60.dp)
                        .height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                }
            }
        }
    }
}
