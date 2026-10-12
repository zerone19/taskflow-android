package com.taskflow.app.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * UI test untuk TaskCard — komponen inti Home list.
 * Memverifikasi: rendering title/description/priority chip, checkbox
 * accessibility, dan callback toggle & click.
 */
class TaskCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun sampleTask() = Task(
        id = 1,
        title = "Refactor Auth Flow",
        description = "Migrate token storage to EncryptedSharedPreferences",
        priority = Priority.HIGH
    )

    @Test
    fun taskCard_displaysTitleDescriptionAndPriority() {
        composeTestRule.setContent {
            TaskCard(task = sampleTask(), onToggleComplete = {}, onClick = {})
        }

        composeTestRule.onNodeWithText("Refactor Auth Flow").assertIsDisplayed()
        composeTestRule.onNodeWithText("Migrate token storage to EncryptedSharedPreferences")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("P0 - High").assertIsDisplayed()
    }

    @Test
    fun taskCard_checkboxHasContentDescription() {
        composeTestRule.setContent {
            TaskCard(task = sampleTask(), onToggleComplete = {}, onClick = {})
        }

        composeTestRule.onNodeWithContentDescription("Mark complete").assertIsDisplayed()
    }

    @Test
    fun taskCard_completedCheckboxShowsMarkIncompleteDescription() {
        composeTestRule.setContent {
            TaskCard(
                task = sampleTask().copy(completed = true),
                onToggleComplete = {},
                onClick = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Mark incomplete").assertIsDisplayed()
    }

    @Test
    fun taskCard_toggleClickInvokesCallback() {
        var toggled: Task? = null
        composeTestRule.setContent {
            TaskCard(task = sampleTask(), onToggleComplete = { toggled = it }, onClick = {})
        }

        composeTestRule.onNodeWithContentDescription("Mark complete").performClick()
        assertEquals(sampleTask().title, toggled?.title)
        assertEquals(sampleTask().id, toggled?.id)
    }

    @Test
    fun taskCard_clickInvokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            TaskCard(task = sampleTask(), onToggleComplete = {}, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Refactor Auth Flow").performClick()
        assertTrue(clicked)
    }
}
