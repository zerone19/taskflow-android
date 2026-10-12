package com.taskflow.app.ui.form

import com.taskflow.app.data.local.testing.FakeTaskDao
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.DefaultTaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Test untuk TaskFormViewModel — validasi mode add/edit & state form.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TaskFormViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: DefaultTaskRepository
    private lateinit var dao: FakeTaskDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dao = FakeTaskDao()
        repository = DefaultTaskRepository(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty form`() = runTest(testDispatcher) {
        val vm = TaskFormViewModel(repository, taskId = null)
        val state = vm.formState.value
        assertEquals("", state.title)
        assertEquals("", state.description)
        assertEquals(Priority.MEDIUM, state.priority)
        assertNull(state.titleError)
        assertTrue(!state.isSaving)
    }

    @Test
    fun `edit mode loads existing task`() = runTest(testDispatcher) {
        val id = repository.insertTask(
            Task(title = "Existing Task", description = "desc", priority = Priority.HIGH)
        )

        val vm = TaskFormViewModel(repository, taskId = id)
        advanceUntilIdle()

        val state = vm.formState.value
        assertEquals("Existing Task", state.title)
        assertEquals("desc", state.description)
        assertEquals(Priority.HIGH, state.priority)
    }

    @Test
    fun `save with blank title sets error and does not persist`() = runTest(testDispatcher) {
        val vm = TaskFormViewModel(repository, taskId = null)
        var successCalled = false

        vm.save { successCalled = true }
        advanceUntilIdle()

        assertEquals("Title is required", vm.formState.value.titleError)
        assertTrue("save success tidak boleh terpanggil", !successCalled)
        assertTrue("task tidak boleh tersimpan", dao.getTasksSync().isEmpty())
    }

    @Test
    fun `save in add mode inserts new task`() = runTest(testDispatcher) {
        val vm = TaskFormViewModel(repository, taskId = null)
        var successCalled = false

        vm.onTitleChange("New Task")
        vm.onPriorityChange(Priority.LOW)
        vm.save { successCalled = true }
        advanceUntilIdle()

        assertTrue("onSuccess harus terpanggil", successCalled)
        val tasks = dao.getTasksSync()
        assertEquals(1, tasks.size)
        assertEquals("New Task", tasks[0].title)
        assertEquals(Priority.LOW, tasks[0].priority)
    }

    @Test
    fun `save in edit mode updates existing task`() = runTest(testDispatcher) {
        val id = repository.insertTask(Task(title = "Original"))
        val vm = TaskFormViewModel(repository, taskId = id)
        advanceUntilIdle()

        vm.onTitleChange("Updated Title")
        vm.save {}
        advanceUntilIdle()

        val tasks = dao.getTasksSync()
        assertEquals("harus tetap 1 task (update, bukan insert baru)", 1, tasks.size)
        assertEquals("Updated Title", tasks[0].title)
        assertEquals(id, tasks[0].id)
    }
}
