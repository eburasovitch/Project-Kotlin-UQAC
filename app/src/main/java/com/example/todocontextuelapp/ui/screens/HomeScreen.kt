package com.example.todocontextuelapp.ui.screens

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

@Composable
fun HomeScreen(
    routines: List<Routine>,
    onCreateClicked: () -> Unit,
    onEditClicked: (Routine) -> Unit,
    onDeleteClicked: (Routine) -> Unit,
    onToggleCompletion: (Int, Boolean) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClicked) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(routines) { routine ->
                    TaskCard(
                        routine = routine,
                        onEditClicked = { onEditClicked(routine) },
                        onDeleteClicked = { onDeleteClicked(routine) },
                        onCheckedChange = { isChecked ->
                            onToggleCompletion(routine.id ?: 0, isChecked)
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
    var checked by remember(routine.id) { mutableStateOf(false) }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
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
