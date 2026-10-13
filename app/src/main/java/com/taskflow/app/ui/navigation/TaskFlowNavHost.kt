package com.taskflow.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.ui.detail.TaskDetailScreen
import com.taskflow.app.ui.form.TaskFormScreen
import com.taskflow.app.ui.home.HomeScreen

@Composable
fun TaskFlowNavHost(taskRepository: TaskRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                taskRepository = taskRepository,
                onTaskClick = { taskId ->
                    navController.navigate(Routes.taskDetail(taskId))
                },
                onAddTaskClick = {
                    navController.navigate(Routes.taskForm())
                }
            )
        }
        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailScreen(
                taskRepository = taskRepository,
                taskId = taskId,
                onEditClick = { id ->
                    navController.navigate(Routes.taskForm(id))
                },
                onBackClick = {
                    navController.popBackStack()
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
