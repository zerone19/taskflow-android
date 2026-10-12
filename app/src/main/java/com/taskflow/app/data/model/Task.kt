package com.taskflow.app.data.model

/**
 * Priority level untuk Task.
 * Mengikuti DATA_MODEL.md: LOW, MEDIUM, HIGH.
 */
enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Domain model untuk Task.
 * Field opsional (dueDate, tags, assignee, storyPoints) tersedia untuk masa depan
 * tapi tidak dirender/divalidasi di UI v1 (sesuai requirements dasar).
 */
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
    // Field opsional untuk masa depan (tidak dirender di v1):
    // val dueDate: Long? = null,
    // val tags: List<String> = emptyList(),
    // val assignee: String? = null,
    // val storyPoints: Int? = null,
)
