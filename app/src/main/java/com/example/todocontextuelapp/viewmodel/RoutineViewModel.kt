package com.example.todocontextuelapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocontextuelapp.data.Routine
import com.example.todocontextuelapp.data.RoutineDatabase
import com.example.todocontextuelapp.data.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    // Le repository par défaut, si aucun n’est injecté
    protected open lateinit var repository: RoutineRepository

    init {
        // Instancie le repo
        val dao = RoutineDatabase.getInstance(application).routineDao
        repository = RoutineRepository(dao)
    }

    // SECOND CONSTRUCTEUR : si vous voulez injecter un repo pour des tests
    constructor(application: Application, injectedRepo: RoutineRepository) : this(application) {
        repository = injectedRepo
    }

    val allRoutines: Flow<List<Routine>> get() = repository.allRoutines

    fun insertRoutine(routine: Routine) = viewModelScope.launch {
        repository.insert(routine)
    }

    fun updateRoutine(routine: Routine) = viewModelScope.launch {
        repository.update(routine)
    }

    fun deleteRoutine(routine: Routine) = viewModelScope.launch {
        repository.delete(routine)
    }

    fun getRoutineById(id: Int, callback: (Routine?) -> Unit) = viewModelScope.launch {
        val routine = repository.getRoutineById(id)
        callback(routine)
    }
}
