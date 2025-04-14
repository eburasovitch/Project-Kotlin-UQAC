package com.example.todocontextuelapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todocontextuelapp.data.Routine
import java.util.Locale
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.toArgb
import android.graphics.Color as AndroidColor
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme


@Composable
fun HomeScreen(
    routines: List<Routine>,
    onCreateClicked: () -> Unit,
    onEditClicked: (Routine) -> Unit,
    onDeleteClicked: (Routine) -> Unit,
    onToggleCompletion: (Int, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Todo tasks",
                        color = Color.Black,
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateClicked,
                backgroundColor = Color.Black,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Regroupement des tâches par catégorie
            val groupedRoutines = routines.groupBy { it.category }
            LazyColumn {
                groupedRoutines.forEach { (category, routinesInCategory) ->
                    // En-tête de catégorie
                    item {
                        Text(
                            text = category,
                            style = androidx.compose.material.MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                    items(routinesInCategory) { routine ->
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
}
fun Color.adjustSaturation(factor: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(this.toArgb(), hsv)
    hsv[1] = (hsv[1] * factor).coerceIn(0f, 0.5f)  // factor entre 0 (pas de saturation) et 1 (saturation originale)
    return Color(AndroidColor.HSVToColor(hsv))
}
@Composable
fun TaskCard(
    routine: Routine,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var checked by remember(routine.id) { mutableStateOf(false) }

    // Choix de la couleur en fonction de la priorité
    val priorityColor = when (routine.priority.lowercase(Locale.getDefault())) {
        "high" -> Color(0xFFCB4545).adjustSaturation(0.8f)
        "medium" -> Color(0xFFCB8845).adjustSaturation(0.8f) // Orange moins saturé
        "low" -> Color(0xFF77A977).adjustSaturation(0.8f)
        else -> Color.LightGray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                BorderStroke(2.dp, priorityColor),
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = 2.dp,
        backgroundColor = Color.White
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
                        style = androidx.compose.material.MaterialTheme.typography.body1.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Set for: ${routine.hour} ${routine.amPm}",
                        style = androidx.compose.material.MaterialTheme.typography.caption
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
