package com.taskflow.app.ui.navigation

/**
 * Route definitions untuk Navigation Compose.
 * Pass ID saja (lightweight), bukan objek Task penuh.
 */
object Routes {
    const val HOME = "home"
    const val TASK_DETAIL = "task_detail/{taskId}"
    const val TASK_FORM = "task_form?taskId={taskId}"

    fun taskDetail(taskId: Long): String = "task_detail/$taskId"
    fun taskForm(taskId: Long? = null): String =
        if (taskId != null) "task_form?taskId=$taskId" else "task_form"
}
