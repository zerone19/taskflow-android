package com.taskflow.app.data.repository

import com.taskflow.app.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction untuk Task data.
 * Hanya sumber lokal (Room) — tidak ada backend (sesuai API_CONTRACT.md).
 */
interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun searchTasks(query: String): Flow<List<Task>>
    suspend fun getTaskById(id: Long): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteExpiredTasks(now: Long)
}
