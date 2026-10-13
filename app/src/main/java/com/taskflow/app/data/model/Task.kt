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
    val updatedAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,       // epoch millis; null = tidak ada deadline
    val tags: List<String> = emptyList(),  // disimpan sebagai comma-separated String di DB
    val expiresAt: Long? = null      // epoch millis; null = tidak ada expiry
)
