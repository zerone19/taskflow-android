package com.taskflow.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TaskDetailUiState {
    data object Loading : TaskDetailUiState
    data class Success(val task: Task) : TaskDetailUiState
    data class Error(val message: String) : TaskDetailUiState
    data object Deleted : TaskDetailUiState
}

class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val taskId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow<TaskDetailUiState>(TaskDetailUiState.Loading)
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    /** Emit task yang baru dihapus, untuk fitur Undo Delete (re-insert). */
    private val _deletedTask = MutableSharedFlow<Task>(extraBufferCapacity = 1)
    val deletedTask: SharedFlow<Task> = _deletedTask.asSharedFlow()

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            try {
                val task = taskRepository.getTaskById(taskId)
                if (task != null) {
                    _uiState.value = TaskDetailUiState.Success(task)
                } else {
                    _uiState.value = TaskDetailUiState.Error("Task not found")
                }
            } catch (e: Exception) {
                _uiState.value = TaskDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleComplete() {
        val current = _uiState.value
        if (current is TaskDetailUiState.Success) {
            viewModelScope.launch {
                val updated = current.task.copy(
                    completed = !current.task.completed,
                    updatedAt = System.currentTimeMillis()
                )
                taskRepository.updateTask(updated)
                _uiState.value = TaskDetailUiState.Success(updated)
            }
        }
    }

    /**
     * Hapus task. Setelah penghapusan berhasil dikomit, state menjadi
     * Deleted supaya Screen bisa pop dengan aman TANPA race dengan Room.
     * Sebelumnya Screen memanggil deleteTask() lalu langsung popBackStack()
     * tanpa sinkronisasi — penghapusan bisa belum selesai saat screen ditutup.
     */
    fun deleteTask() {
        val current = _uiState.value
        if (current is TaskDetailUiState.Success) {
            viewModelScope.launch {
                try {
                    taskRepository.deleteTask(current.task)
                    _deletedTask.tryEmit(current.task)  // untuk Undo
                    _uiState.value = TaskDetailUiState.Deleted
                } catch (e: Exception) {
                    _uiState.value = TaskDetailUiState.Error(
                        e.message ?: "Failed to delete task"
                    )
                }
            }
        }
    }
}

class TaskDetailViewModelFactory(
    private val taskRepository: TaskRepository,
    private val taskId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
            return TaskDetailViewModel(taskRepository, taskId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
