package com.taskflow.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.ui.detail.TaskDetailScreen
import com.taskflow.app.ui.form.TaskFormScreen
import com.taskflow.app.ui.home.HomeScreen

@Composable
fun TaskFlowNavHost(
    taskRepository: TaskRepository,
    subtaskRepository: com.taskflow.app.data.repository.SubtaskRepository,
    isDarkMode: Boolean = false,
    onToggleTheme: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) { backStackEntry ->
            // Baca deleted_task dari savedStateHandle untuk Undo Snackbar.
            val deletedTask by backStackEntry.savedStateHandle
                .getStateFlow<Task?>("deleted_task", null)
                .collectAsStateWithLifecycle()

            HomeScreen(
                taskRepository = taskRepository,
                onTaskClick = { taskId ->
                    navController.navigate(Routes.taskDetail(taskId))
                },
                onAddTaskClick = {
                    navController.navigate(Routes.taskForm())
                },
                deletedTask = deletedTask,
                onUndoConsumed = {
                    backStackEntry.savedStateHandle["deleted_task"] = null
                },
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme
            )
        }
        composable(Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailScreen(
                taskRepository = taskRepository,
                subtaskRepository = subtaskRepository,
                taskId = taskId,
                onEditClick = { id ->
                    navController.navigate(Routes.taskForm(id))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onTaskDeleted = { deletedTask ->
                    // Tampilkan Undo Snackbar di HomeScreen via savedStateHandle
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("deleted_task", deletedTask)
                }
            )
        }

        composable(
            route = Routes.TASK_FORM,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskFormScreen(
                taskRepository = taskRepository,
                taskId = if (taskId == -1L) null else taskId,
                onSaveClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
