package com.example.todocontextuelapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todocontextuelapp.data.Routine

@Composable
fun HomeScreen(
    routines: List<Routine>,
    onCreateClicked: () -> Unit,
    onEditClicked: (Routine) -> Unit,
    onDeleteClicked: (Routine) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Todo tasks") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClicked) {
                Text("+")
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
                        onDeleteClicked = { onDeleteClicked(routine) }
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
    onDeleteClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = routine.name,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Set for: ${routine.hour} ${routine.amPm}",
                    style = MaterialTheme.typography.caption
                )
            }
            Row {
                IconButton(onClick = onEditClicked) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClicked) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
