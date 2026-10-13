package com.taskflow.app.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TaskFormState(
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val tags: List<String> = emptyList(),
    val titleError: String? = null,
    val saveError: String? = null,
    val isSaving: Boolean = false
)

class TaskFormViewModel(
    private val taskRepository: TaskRepository,
    private val taskId: Long? = null  // null = mode add, non-null = mode edit
) : ViewModel() {

    private val _formState = MutableStateFlow(TaskFormState())
    val formState: StateFlow<TaskFormState> = _formState.asStateFlow()

    init {
        if (taskId != null) {
            loadTask()
        }
    }

    private fun loadTask() {
        val id = taskId ?: return  // non-null lokal — aman di dalam lambda (tanpa !!)
        viewModelScope.launch {
            val task = taskRepository.getTaskById(id)
            if (task != null) {
                _formState.value = TaskFormState(
                    title = task.title,
                    description = task.description,
                    priority = task.priority,
                    dueDate = task.dueDate,
                    tags = task.tags
                )
            }
        }
    }

    fun onTitleChange(title: String) {
        _formState.value = _formState.value.copy(title = title, titleError = null)
    }

    fun onDescriptionChange(description: String) {
        _formState.value = _formState.value.copy(description = description)
    }

    fun onPriorityChange(priority: Priority) {
        _formState.value = _formState.value.copy(priority = priority)
    }

    fun onDueDateChange(dueDate: Long?) {
        _formState.value = _formState.value.copy(dueDate = dueDate)
    }

    fun onTagToggle(tag: String) {
        val current = _formState.value.tags
        val updated = if (tag in current) current - tag else current + tag
        _formState.value = _formState.value.copy(tags = updated)
    }

    fun save(onSuccess: () -> Unit) {
        val state = _formState.value

        // Validasi
        if (state.title.isBlank()) {
            _formState.value = state.copy(titleError = "Title is required")
            return
        }

        _formState.value = state.copy(isSaving = true, saveError = null)

        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()
                if (taskId != null) {
                    // Mode edit
                    val existing = taskRepository.getTaskById(taskId)
                    if (existing != null) {
                        taskRepository.updateTask(
                            existing.copy(
                                title = state.title,
                                description = state.description,
                                priority = state.priority,
                                dueDate = state.dueDate,
                                tags = state.tags,
                                updatedAt = now
                            )
                        )
                    }
                } else {
                    // Mode add
                    taskRepository.insertTask(
                        Task(
                            title = state.title,
                            description = state.description,
                            priority = state.priority,
                            dueDate = state.dueDate,
                            tags = state.tags,
                            createdAt = now,
                            updatedAt = now
                        )
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                // Jangan telan error — tampilkan ke user supaya bisa retry.
                _formState.value = state.copy(
                    isSaving = false,
                    saveError = e.message ?: "Failed to save task"
                )
            }
        }
    }
}

class TaskFormViewModelFactory(
    private val taskRepository: TaskRepository,
    private val taskId: Long? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskFormViewModel::class.java)) {
            return TaskFormViewModel(taskRepository, taskId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
