package com.example.todocontextuelapp.data

data class Routine(
    val id: Int = 0,
    val name: String,
    val description: String,
    val date: String,
    val hour: Int,
    val amPm: String,
    val frequency: String,
    val completed: Boolean = false
)