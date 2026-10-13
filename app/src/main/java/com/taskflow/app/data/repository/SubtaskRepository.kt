package com.taskflow.app.data.repository

import com.taskflow.app.data.local.SubtaskDao
import com.taskflow.app.data.local.SubtaskEntity
import com.taskflow.app.data.model.Subtask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository abstraction untuk Subtask data.
 */
interface SubtaskRepository {
    fun getSubtasksForTask(taskId: Long): Flow<List<Subtask>>
    suspend fun insertSubtask(subtask: Subtask): Long
    suspend fun updateSubtask(subtask: Subtask)
    suspend fun deleteSubtasksForTask(taskId: Long)
}

class DefaultSubtaskRepository(
    private val subtaskDao: SubtaskDao
) : SubtaskRepository {

    override fun getSubtasksForTask(taskId: Long): Flow<List<Subtask>> {
        return subtaskDao.getSubtasksForTask(taskId).map { entities ->
            entities.map { Subtask(id = it.id, taskId = it.taskId, title = it.title, completed = it.completed) }
        }
    }

    override suspend fun insertSubtask(subtask: Subtask): Long {
        return subtaskDao.insertSubtask(
            SubtaskEntity(
                id = subtask.id,
                taskId = subtask.taskId,
                title = subtask.title,
                completed = subtask.completed
            )
        )
    }

    override suspend fun updateSubtask(subtask: Subtask) {
        subtaskDao.updateSubtask(
            SubtaskEntity(
                id = subtask.id,
                taskId = subtask.taskId,
                title = subtask.title,
                completed = subtask.completed
            )
        )
    }

    override suspend fun deleteSubtasksForTask(taskId: Long) {
        subtaskDao.deleteSubtasksForTask(taskId)
    }
}
