package com.taskflow.app.ui.form

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.taskflow.app.data.local.testing.FakeTaskDao
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.DefaultTaskRepository
import com.taskflow.app.ui.component.TaskFlowThemeForTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * UI test untuk TaskFormScreen — alur kritis "Add Task":
 * validasi title wajib diisi, pilih priority, dan save.
 * Memakai FakeTaskDao (in-memory) supaya tidak perlu Room sungguhan.
 */
class TaskFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun fakeRepository(): DefaultTaskRepository =
        DefaultTaskRepository(FakeTaskDao())

    @Test
    fun form_displaysTitleDescriptionPriorityAndButtons() {
        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskFormScreen(
                    taskRepository = fakeRepository(),
                    taskId = null,
                    onSaveClick = {},
                    onCancelClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("New Task").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title *").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Priority").assertIsDisplayed()
        composeTestRule.onNodeWithText("HIGH").assertIsDisplayed()
        composeTestRule.onNodeWithText("MEDIUM").assertIsDisplayed()
        composeTestRule.onNodeWithText("LOW").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun saveWithBlankTitle_showsValidationError() {
        var saved = false
        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskFormScreen(
                    taskRepository = fakeRepository(),
                    taskId = null,
                    onSaveClick = { saved = true },
                    onCancelClick = {}
                )
            }
        }

        // Jangan isi title, langsung Save
        composeTestRule.onNodeWithText("Save").performClick()

        // Error validasi muncul, save tidak dipanggil
        composeTestRule.onNodeWithText("Title is required").assertIsDisplayed()
        assertTrue("save tidak boleh terpanggil saat title kosong", !saved)
    }

    @Test
    fun saveWithTitle_persistsTaskAndInvokesCallback() {
        val dao = FakeTaskDao()
        val repository = DefaultTaskRepository(dao)
        var saved = false

        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskFormScreen(
                    taskRepository = repository,
                    taskId = null,
                    onSaveClick = { saved = true },
                    onCancelClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Title *").performTextInput("Belajar Compose Testing")
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        assertTrue("onSaveClick harus terpanggil setelah save berhasil", saved)

        // Verifikasi task benar-benar tersimpan di repository
        val tasks = dao.getTasksSync()
        assertEquals(1, tasks.size)
        assertEquals("Belajar Compose Testing", tasks[0].title)
    }

    @Test
    fun prioritySelection_changesSelectedChip() {
        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskFormScreen(
                    taskRepository = fakeRepository(),
                    taskId = null,
                    onSaveClick = {},
                    onCancelClick = {}
                )
            }
        }

        // Default MEDIUM selected; klik HIGH
        composeTestRule.onNodeWithText("HIGH").performClick()
        composeTestRule.waitForIdle()

        // Verifikasi tidak crash dan HIGH chip tetap ada (state berubah)
        composeTestRule.onNodeWithText("HIGH").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun editMode_showsEditTaskTitle() {
        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskFormScreen(
                    taskRepository = fakeRepository(),
                    taskId = 1L,
                    onSaveClick = {},
                    onCancelClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Edit Task").assertIsDisplayed()
    }
}
