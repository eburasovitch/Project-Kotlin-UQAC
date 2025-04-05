package com.example.todocontextuelapp.viewmodel

import com.example.todocontextuelapp.data.Routine
import com.example.todocontextuelapp.data.RoutineDao
import com.example.todocontextuelapp.data.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// ViewModel local de test (sans AndroidViewModel)
class LocalRoutineViewModel(private val repository: RoutineRepository) {
    val allRoutines: Flow<List<Routine>> = repository.allRoutines

    suspend fun insertRoutine(routine: Routine) {
        repository.insert(routine)
    }

    suspend fun updateRoutine(routine: Routine) {
        repository.update(routine)
    }

    suspend fun deleteRoutine(routine: Routine) {
        repository.delete(routine)
    }

    suspend fun getRoutineById(id: Int): Routine? {
        return repository.getRoutineById(id)
    }
}

// DAO factice en mémoire
class FakeRoutineDao : RoutineDao {
    private val inMemoryList = mutableListOf<Routine>()
    private val routinesFlow = MutableStateFlow<List<Routine>>(emptyList())

    override fun getAllRoutines(): Flow<List<Routine>> {
        return routinesFlow
    }

    override suspend fun getRoutineById(id: Int): Routine? {
        return inMemoryList.firstOrNull { it.id == id }
    }

    override suspend fun insertRoutine(routine: Routine): Long {
        val nextId = (inMemoryList.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
        inMemoryList.add(routine.copy(id = nextId))
        routinesFlow.value = inMemoryList.toList()
        return nextId.toLong()
    }

    override suspend fun updateRoutine(routine: Routine): Int {
        val index = inMemoryList.indexOfFirst { it.id == routine.id }
        if (index != -1) {
            inMemoryList[index] = routine
            routinesFlow.value = inMemoryList.toList()
            return 1
        }
        return 0
    }

    override suspend fun deleteRoutine(routine: Routine): Int {
        val removed = inMemoryList.removeAll { it.id == routine.id }
        if (removed) {
            routinesFlow.value = inMemoryList.toList()
            return 1
        }
        return 0
    }
}

// Test complet CRUD
class RoutineViewModelLocalTest {

    private lateinit var viewModel: LocalRoutineViewModel
    private lateinit var fakeDao: FakeRoutineDao
    private lateinit var repository: RoutineRepository

    @Before
    fun setUp() {
        fakeDao = FakeRoutineDao()
        repository = RoutineRepository(fakeDao)
        viewModel = LocalRoutineViewModel(repository)
    }

    @Test
    fun `routine should be inserted`() = runBlocking {
        val routine = Routine(
            name = "Test Routine",
            description = "Pour tester l'insertion",
            date = "12/31/2025",
            hour = 10,
            minute = 0,
            amPm = "AM",
            frequency = "Just for this time"
        )
        viewModel.insertRoutine(routine)
        val all = viewModel.allRoutines.first()
        assertEquals(1, all.size)
        assertEquals("Test Routine", all[0].name)
    }

    @Test
    fun `routine should be updated`() = runBlocking {
        val routine = Routine(
            name = "Old Name",
            description = "Desc",
            date = "01/01/2026",
            hour = 8,
            minute = 0,
            amPm = "PM",
            frequency = "Every day"
        )
        viewModel.insertRoutine(routine)
        val inserted = viewModel.allRoutines.first().first()

        val updated = inserted.copy(name = "Updated Name")
        viewModel.updateRoutine(updated)
        val all = viewModel.allRoutines.first()
        assertEquals(1, all.size)
        assertEquals("Updated Name", all[0].name)
    }

    @Test
    fun `routine should be deleted`() = runBlocking {
        val routine = Routine(
            name = "To delete",
            description = "Desc",
            date = "02/02/2026",
            hour = 9,
            minute = 0,
            amPm = "AM",
            frequency = "Once a week"
        )
        viewModel.insertRoutine(routine)
        val inserted = viewModel.allRoutines.first().first()

        viewModel.deleteRoutine(inserted)
        val all = viewModel.allRoutines.first()
        assertEquals(0, all.size)
    }
}
