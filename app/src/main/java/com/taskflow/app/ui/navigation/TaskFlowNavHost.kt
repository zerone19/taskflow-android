package com.taskflow.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.SubtaskRepository
import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.ui.component.PlaceholderScreen
import com.taskflow.app.ui.detail.TaskDetailScreen
import com.taskflow.app.ui.form.TaskFormScreen
import com.taskflow.app.ui.home.HomeScreen

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomTabs = listOf(
    BottomTab(Routes.HOME, "TASK", Icons.Filled.Checklist),
    BottomTab(Routes.DATE, "DATE", Icons.Filled.CalendarMonth),
    BottomTab(Routes.AI_CHAT, "AI CHAT", Icons.Filled.SmartToy),
    BottomTab(Routes.SETTINGS, "SETTING", Icons.Filled.Settings)
)

@Composable
fun TaskFlowNavHost(
    taskRepository: TaskRepository,
    subtaskRepository: SubtaskRepository,
    isDarkMode: Boolean = false,
    onToggleTheme: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom nav hanya tampil di top-level destinations (tab), bukan detail/form.
    val showBottomBar = currentRoute in bottomTabs.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                if (currentRoute != tab.route) {
                                    navController.navigate(tab.route) {
                                        // M3 pattern: single top + pop to start + save/restore state
                                        popUpTo(Routes.HOME) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

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

        composable(Routes.DATE) {
            PlaceholderScreen(title = "Date")
        }

        composable(Routes.AI_CHAT) {
            PlaceholderScreen(title = "AI Chat")
        }

        composable(Routes.SETTINGS) {
            PlaceholderScreen(title = "Settings")
        }
        }
    }
}
