package com.example.todocontextuelapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_table")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,  // Auto-génération

    val name: String,
    val description: String,
    val date: String,

    // On gère désormais l'heure et la minute séparément
    val hour: Int,
    val minute: Int,
    val amPm: String,
    val frequency: String,
    val completed: Boolean = false,

    // Champs supplémentaires pour la catégorisation et la priorité
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isGeofenceEnabled: Boolean = false,
    val category: String = "General",
    val priority: String = "Low"
)
