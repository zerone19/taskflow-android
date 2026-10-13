package com.taskflow.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.data.repository.DefaultTaskRepository

/**
 * WorkManager worker — hapus task expired secara periodik.
 * Task yang completed = exempt (tidak ikut terhapus).
 * Task tanpa expiresAt (null) = tidak terpengaruh.
 */
class ExpiredTaskWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = TaskDatabase.getDatabase(applicationContext)
        val repository = DefaultTaskRepository(database.taskDao())
        return try {
            repository.deleteExpiredTasks(System.currentTimeMillis())
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "expired_task_cleanup"
    }
}
