package com.taskflow.app.ui.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.taskflow.app.data.local.testing.FakeTaskDao
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.DefaultTaskRepository
import com.taskflow.app.ui.component.TaskFlowThemeForTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * UI test untuk TaskDetailScreen — alur kritis:
 * tampilkan detail task, toggle complete, delete dengan konfirmasi dialog.
 */
class TaskDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun repoWithTask(): Triple<DefaultTaskRepository, FakeTaskDao, Long> {
        val dao = FakeTaskDao()
        val repo = DefaultTaskRepository(dao)
        val id = dao.insertTaskSync(Task(title = "Refactor Auth", priority = Priority.HIGH))
        return Triple(repo, dao, id)
    }

    @Test
    fun detail_displaysTaskTitleAndActions() {
        val (repo, _, id) = repoWithTask()

        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskDetailScreen(
                    taskRepository = repo,
                    taskId = id,
                    onEditClick = {},
                    onBackClick = {}
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Task Detail").assertIsDisplayed()
        composeTestRule.onNodeWithText("Refactor Auth").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mark Complete").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Edit").assertIsDisplayed()
    }

    @Test
    fun markComplete_togglesToMarkIncomplete() {
        val (repo, _, id) = repoWithTask()

        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskDetailScreen(
                    taskRepository = repo,
                    taskId = id,
                    onEditClick = {},
                    onBackClick = {}
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Mark Complete").performClick()
        composeTestRule.waitForIdle()

        // Setelah toggle, label berubah
        composeTestRule.onNodeWithText("Mark Incomplete").assertIsDisplayed()
    }

    @Test
    fun delete_showsConfirmationDialogBeforeDeleting() {
        val (repo, dao, id) = repoWithTask()
        var backClicked = false

        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskDetailScreen(
                    taskRepository = repo,
                    taskId = id,
                    onEditClick = {},
                    onBackClick = { backClicked = true }
                )
            }
        }

        composeTestRule.waitForIdle()

        // Klik Delete (tombol outlined di layar detail) — dialog konfirmasi harus muncul
        composeTestRule.onAllNodesWithText("Delete")[0].performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Delete Task").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Are you sure you want to delete this task? This action cannot be undone."
        ).assertIsDisplayed()

        // Sebelum konfirmasi: task belum terhapus
        assertFalse("task belum boleh terhapus sebelum konfirmasi", dao.getTasksSync().isEmpty())

        // Konfirmasi delete — klik tombol Delete di DALAM dialog (node ke-2)
        composeTestRule.onAllNodesWithText("Delete")[1].performClick()
        composeTestRule.waitForIdle()

        // Task terhapus + back dipanggil
        assertTrue("task harus terhapus setelah konfirmasi", dao.getTasksSync().isEmpty())
        assertTrue("onBackClick harus dipanggil setelah delete", backClicked)
    }

    @Test
    fun deleteDialog_cancelDoesNotDelete() {
        val (repo, dao, id) = repoWithTask()

        composeTestRule.setContent {
            TaskFlowThemeForTest {
                TaskDetailScreen(
                    taskRepository = repo,
                    taskId = id,
                    onEditClick = {},
                    onBackClick = {}
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onAllNodesWithText("Delete")[0].performClick()
        composeTestRule.waitForIdle()

        // Klik Cancel di dialog
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.waitForIdle()

        // Task masih ada
        assertFalse("task harus tetap ada setelah cancel delete", dao.getTasksSync().isEmpty())
    }
}
