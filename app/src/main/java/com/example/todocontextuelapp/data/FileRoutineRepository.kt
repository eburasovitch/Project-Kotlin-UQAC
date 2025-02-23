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
    // État interne pour la liste des routines
    private val routinesFlow = MutableStateFlow<List<Routine>>(emptyList())

    // Nom du fichier JSON
    private val fileName = "routines.json"
    private val gson = Gson()

    init {
        // Charger le contenu du fichier au démarrage
        loadFromFile()
    }

    /**
     * Flux des routines (pour être collecté dans la ViewModel)
     */
    val allRoutines: Flow<List<Routine>>
        get() = routinesFlow

    /**
     * Charge la liste de routines depuis le fichier JSON
     */
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

    /**
     * Écrit la liste courante dans le fichier JSON
     */
    private fun saveToFile() {
        val file = File(appContext.filesDir, fileName)
        val json = gson.toJson(routinesFlow.value)
        file.writeText(json)
    }

    /**
     * Insère une nouvelle routine
     */
    suspend fun insert(routine: Routine) {
        val currentList = routinesFlow.value.toMutableList()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        val routineWithId = routine.copy(id = newId)
        Log.d("FileRoutineRepository", "Inserting new routine: $routineWithId")
        currentList.add(routineWithId)
        routinesFlow.value = currentList
        saveToFile()
    }

    /**
     * Met à jour une routine existante
     */
    suspend fun update(routine: Routine) {
        val currentList = routinesFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == routine.id }
        if (index != -1) {
            currentList[index] = routine
            routinesFlow.value = currentList
            saveToFile()
        }
    }

    /**
     * Supprime une routine
     */
    suspend fun delete(routine: Routine) {
        val currentList = routinesFlow.value.toMutableList()
        currentList.removeAll { it.id == routine.id }
        routinesFlow.value = currentList
        saveToFile()
    }

    /**
     * Récupère une routine par ID
     */
    suspend fun getRoutineById(routineId: Int): Routine? {
        Log.d("FileRoutineRepository", "getRoutineById($routineId) called. Current routines = ${routinesFlow.value}")
        return routinesFlow.value.find { it.id == routineId }
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
