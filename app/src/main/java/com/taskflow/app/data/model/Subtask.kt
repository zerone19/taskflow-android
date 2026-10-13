package com.taskflow.app.data.model

/**
 * Domain model untuk Subtask — checklist item yang dimiliki oleh sebuah Task.
 */
data class Subtask(
    val id: Long = 0,
    val taskId: Long,
    val title: String,
    val completed: Boolean = false
)
