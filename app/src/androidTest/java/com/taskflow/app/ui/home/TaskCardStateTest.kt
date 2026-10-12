package com.taskflow.app.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.ui.component.TaskCard
import com.taskflow.app.ui.component.TaskFlowThemeForTest
import org.junit.Rule
import org.junit.Test

/**
 * UI test untuk TaskCard state completed vs incomplete.
 * Memverifikasi ikon checkbox + content description berubah sesuai state.
 */
class TaskCardStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun incompleteTask_showsMarkCompleteCheckbox() {
        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskCard(
                    task = Task(title = "Incomplete", priority = Priority.LOW),
                    onToggleComplete = {},
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Mark complete").assertIsDisplayed()
    }

    @Test
    fun completedTask_showsMarkIncompleteCheckbox() {
        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskCard(
                    task = Task(title = "Done", completed = true, priority = Priority.LOW),
                    onToggleComplete = {},
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Mark incomplete").assertIsDisplayed()
        composeTestRule.onNodeWithText("Done").assertIsDisplayed()
    }
}
