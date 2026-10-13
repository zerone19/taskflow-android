package com.taskflow.app.data.local.testing

import com.taskflow.app.data.local.TaskDao
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.local.toDomain
import com.taskflow.app.data.local.toEntity
import com.taskflow.app.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fake TaskDao untuk unit test — in-memory, tidak perlu Room database.
 * Menyediakan helper sinkron untuk setup data di test (unit & androidTest).
 */
class FakeTaskDao : TaskDao {

    private val tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    private var nextId = 1L

    override fun getAllTasks(): Flow<List<TaskEntity>> = tasks

    override fun searchTasks(query: String): Flow<List<TaskEntity>> {
        val filtered = tasks.value.filter {
            it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
        }
        return MutableStateFlow(filtered)
    }

    override suspend fun getTaskById(id: Long): TaskEntity? {
        return tasks.value.find { it.id == id }
    }

    override suspend fun insertTask(task: TaskEntity): Long {
        val id = nextId++
        val entity = task.copy(id = id)
        tasks.value = tasks.value + entity
        return id
    }

    override suspend fun updateTask(task: TaskEntity) {
        tasks.value = tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasks.value = tasks.value.filter { it.id != task.id }
    }

    /** Helper sinkron untuk setup test — insert langsung tanpa suspend. */
    fun insertTaskSync(task: Task): Long {
        val id = nextId++
        val entity = task.copy(id = id).toEntity()
        tasks.value = tasks.value + entity
        return id
    }

    /** Helper test — ambil semua task sebagai domain model. */
    fun getTasksSync(): List<Task> = tasks.value.map { it.toDomain() }

    /** Hapus semua task (reset antar test). */
    fun clear() {
        tasks.value = emptyList()
        nextId = 1L
    }
}
