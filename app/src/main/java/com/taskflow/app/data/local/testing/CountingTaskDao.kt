package com.taskflow.app.data.local.testing

import com.taskflow.app.data.local.TaskDao
import com.taskflow.app.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Test double yang membungkus FakeTaskDao dan menghitung berapa kali
 * getAllTasks() di-subscribe. Dipakai untuk regression test: setiap kali
 * layar recompose, startObserving() tidak boleh menambah collector baru.
 */
class CountingTaskDao(
    private val delegate: FakeTaskDao = FakeTaskDao()
) : TaskDao {

    private val _subscriptions = MutableStateFlow(0)

    /** Jumlah collector yang dibuat pada getAllTasks() — harus tetap 1 walau startObserving() dipanggil berulang. */
    val subscriptionCount: StateFlow<Int> = _subscriptions

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        _subscriptions.value += 1
        return delegate.getAllTasks()
    }

    override fun searchTasks(query: String): Flow<List<TaskEntity>> = delegate.searchTasks(query)

    override suspend fun getTaskById(id: Long): TaskEntity? = delegate.getTaskById(id)

    override suspend fun insertTask(task: TaskEntity): Long = delegate.insertTask(task)

    override suspend fun updateTask(task: TaskEntity) = delegate.updateTask(task)

    override suspend fun deleteTask(task: TaskEntity) = delegate.deleteTask(task)
}
