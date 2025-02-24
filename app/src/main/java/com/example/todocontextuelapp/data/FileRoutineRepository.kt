package com.example.todocontextuelapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import android.util.Log

class FileRoutineRepository private constructor(
    private val appContext: Context
) {
    private val routinesFlow = MutableStateFlow<List<Routine>>(emptyList())
    private val fileName = "routines.json"
    private val gson = Gson()

    init {
        loadFromFile()
    }

    val allRoutines: Flow<List<Routine>>
        get() = routinesFlow

    private fun loadFromFile() {
        val file = File(appContext.filesDir, fileName)
        if (file.exists()) {
            val content = file.readText()
            if (content.isNotBlank()) {
                val type = object : TypeToken<List<Routine>>() {}.type
                val loadedList: List<Routine> = gson.fromJson(content, type)
                routinesFlow.value = loadedList
            }
        }
    }

    private fun saveToFile() {
        val file = File(appContext.filesDir, fileName)
        val json = gson.toJson(routinesFlow.value)
        file.writeText(json)
    }

    suspend fun insert(routine: Routine) {
        val currentList = routinesFlow.value.toMutableList()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        val routineWithId = routine.copy(id = newId)
        Log.d("FileRoutineRepository", "Inserting new routine: $routineWithId")
        currentList.add(routineWithId)
        routinesFlow.value = currentList
        saveToFile()
    }

    suspend fun update(routine: Routine) {
        val currentList = routinesFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == routine.id }
        if (index != -1) {
            currentList[index] = routine
            routinesFlow.value = currentList
            saveToFile()
        }
    }

    suspend fun delete(routine: Routine) {
        val currentList = routinesFlow.value.toMutableList()
        currentList.removeAll { it.id == routine.id }
        routinesFlow.value = currentList
        saveToFile()
    }

    suspend fun getRoutineById(routineId: Int): Routine? {
        Log.d("FileRoutineRepository", "getRoutineById($routineId) called. Current routines = ${routinesFlow.value}")
        return routinesFlow.value.find { it.id == routineId }
    }

    suspend fun toggleRoutineCompletion(id: Int, isCompleted: Boolean) {
        val routine = getRoutineById(id)
        if (routine != null) {
            val updatedRoutine = routine.copy(completed = isCompleted)
            update(updatedRoutine)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FileRoutineRepository? = null

        fun getInstance(context: Context): FileRoutineRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FileRoutineRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}