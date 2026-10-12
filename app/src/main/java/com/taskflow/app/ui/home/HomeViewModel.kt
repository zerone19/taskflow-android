package com.taskflow.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

enum class TaskFilter {
    ALL, PENDING, HIGH_PRIORITY, COMPLETED
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(
        val tasks: List<Task> = emptyList(),
        val filter: TaskFilter = TaskFilter.ALL
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var allTasks: List<Task> = emptyList()

    @Volatile
    private var isObserving = false

    init {
        // Tidak langsung load — tunggu sampai ViewModel benar-benar digunakan.
        // Ini memungkinkan test untuk memverifikasi initial state = Loading.
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks()
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
                .collect { tasks ->
                    allTasks = tasks
                    val currentFilter = (_uiState.value as? HomeUiState.Success)?.filter
                        ?: TaskFilter.ALL
                    val filtered = applyFilter(tasks, currentFilter)
                    _uiState.value = if (filtered.isEmpty() && tasks.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(tasks = filtered, filter = currentFilter)
                    }
                }
        }
    }

    /**
     * Mulai observe tasks dari repository.
     * Idempotent — pemanggilan berulang (misalnya akibat recomposition Compose)
     * tidak akan membuat collector Flow tambahan.
     */
    fun startObserving() {
        if (isObserving) return
        isObserving = true
        loadTasks()
    }

    fun setFilter(filter: TaskFilter) {
        val filtered = applyFilter(allTasks, filter)
        _uiState.value = if (filtered.isEmpty() && allTasks.isEmpty()) {
            HomeUiState.Empty
        } else {
            HomeUiState.Success(tasks = filtered, filter = filter)
        }
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            val updated = task.copy(
                completed = !task.completed,
                updatedAt = System.currentTimeMillis()
            )
            taskRepository.updateTask(updated)
        }
    }

    private fun applyFilter(tasks: List<Task>, filter: TaskFilter): List<Task> {
        return when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.PENDING -> tasks.filter { !it.completed }
            TaskFilter.HIGH_PRIORITY -> tasks.filter { it.priority == Priority.HIGH }
            TaskFilter.COMPLETED -> tasks.filter { it.completed }
        }
    }
}
