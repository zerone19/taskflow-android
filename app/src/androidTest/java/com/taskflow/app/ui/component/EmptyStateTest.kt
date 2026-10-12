package com.taskflow.app.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * UI test untuk EmptyState — memverifikasi teks desain + CTA.
 */
class EmptyStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyState_displaysHeadlineDescriptionAndCta() {
        composeTestRule.setContent {
            EmptyState(onCreateClick = {})
        }

        composeTestRule.onNodeWithText("No tasks yet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create your first task to get started.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Task").assertIsDisplayed()
    }

    @Test
    fun emptyState_ctaInvokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            EmptyState(onCreateClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Create Task").performClick()
        assertTrue(clicked)
    }
}
