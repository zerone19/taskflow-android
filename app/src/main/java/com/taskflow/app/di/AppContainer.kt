package com.taskflow.app.di

import android.content.Context
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.data.repository.DefaultTaskRepository
import com.taskflow.app.data.repository.TaskRepository

/**
 * Manual Dependency Injection container.
 * Tanpa Hilt/Koin — cukup untuk sandbox (sesuai proposal arsitektur).
 */
interface AppContainer {
    val taskRepository: TaskRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val database = TaskDatabase.getDatabase(context)
    override val taskRepository: TaskRepository = DefaultTaskRepository(database.taskDao())
}
