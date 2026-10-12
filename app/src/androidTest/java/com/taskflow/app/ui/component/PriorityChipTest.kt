package com.taskflow.app.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * UI test untuk PriorityChip — memverifikasi label per level
 * (P0/P1/P2) yang dirender, sesuai DESIGN.md.
 */
class PriorityChipTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun highPriority_showsP0High() {
        composeTestRule.setContent {
            PriorityChip(priority = com.taskflow.app.data.model.Priority.HIGH)
        }

        composeTestRule.onNodeWithText("P0 - High").assertIsDisplayed()
    }

    @Test
    fun mediumPriority_showsP1Medium() {
        composeTestRule.setContent {
            PriorityChip(priority = com.taskflow.app.data.model.Priority.MEDIUM)
        }

        composeTestRule.onNodeWithText("P1 - Medium").assertIsDisplayed()
    }

    @Test
    fun lowPriority_showsP2Low() {
        composeTestRule.setContent {
            PriorityChip(priority = com.taskflow.app.data.model.Priority.LOW)
        }

        composeTestRule.onNodeWithText("P2 - Low").assertIsDisplayed()
    }
}
