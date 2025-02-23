package com.example.todocontextuelapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocontextuelapp.data.FileRoutineRepository
import com.example.todocontextuelapp.data.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FileRoutineRepository = FileRoutineRepository.getInstance(application)

    val allRoutines: Flow<List<Routine>> = repository.allRoutines

    fun addRoutine(routine: Routine) = viewModelScope.launch {
        repository.insert(routine)
    }

    fun updateRoutine(routine: Routine) = viewModelScope.launch {
        repository.update(routine)
    }

    fun deleteRoutine(routine: Routine) = viewModelScope.launch {
        repository.delete(routine)
    }

    fun getRoutineById(id: Int, onResult: (Routine?) -> Unit) {
        viewModelScope.launch {
            val routine = repository.getRoutineById(id)
            onResult(routine)
        }
    }
}
