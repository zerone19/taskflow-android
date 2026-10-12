package com.taskflow.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task

/**
 * Room entity untuk Task.
 * Field sesuai DATA_MODEL.md: id, title, description, priority, completed, createdAt, updatedAt.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: String = "MEDIUM",  // disimpan sebagai String, di-mapping ke enum
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// === Mappers ===

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    priority = Priority.valueOf(priority),
    completed = completed,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    priority = priority.name,
    completed = completed,
    createdAt = createdAt,
    updatedAt = updatedAt
)
