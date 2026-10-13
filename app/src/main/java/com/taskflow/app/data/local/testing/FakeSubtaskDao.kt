package com.taskflow.app.data.local.testing

import com.taskflow.app.data.local.SubtaskDao
import com.taskflow.app.data.local.SubtaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fake SubtaskDao untuk unit test — in-memory, tidak perlu Room database.
 */
class FakeSubtaskDao : SubtaskDao {

    private val subtasks = MutableStateFlow<List<SubtaskEntity>>(emptyList())
    private var nextId = 1L

    override fun getSubtasksForTask(taskId: Long): Flow<List<SubtaskEntity>> {
        return MutableStateFlow(subtasks.value.filter { it.taskId == taskId })
    }

    override suspend fun insertSubtask(subtask: SubtaskEntity): Long {
        val id = nextId++
        val entity = subtask.copy(id = id)
        subtasks.value = subtasks.value + entity
        return id
    }

    override suspend fun updateSubtask(subtask: SubtaskEntity) {
        subtasks.value = subtasks.value.map {
            if (it.id == subtask.id) subtask else it
        }
    }

    override suspend fun deleteSubtasksForTask(taskId: Long) {
        subtasks.value = subtasks.value.filter { it.taskId != taskId }
    }
}
