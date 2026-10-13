package com.taskflow.app.ui.home

import app.cash.turbine.test
import com.taskflow.app.data.local.testing.FakeTaskDao
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import com.taskflow.app.data.repository.DefaultTaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: DefaultTaskRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = DefaultTaskRepository(FakeTaskDao())
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        assertTrue(viewModel.uiState.value is HomeUiState.Loading)
    }

    @Test
    fun `empty state when no tasks`() = runTest(testDispatcher) {
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue("Expected Empty but got $state", state is HomeUiState.Empty)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success state with tasks after insert`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Task 1", priority = Priority.HIGH))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue("Expected Success but got $state", state is HomeUiState.Success)
            assertEquals(1, (state as HomeUiState.Success).tasks.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filter ALL shows all tasks`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "High", priority = Priority.HIGH))
        repository.insertTask(Task(title = "Low", priority = Priority.LOW, completed = true))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setFilter(TaskFilter.ALL)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(2, state.tasks.size)
    }

    @Test
    fun `filter COMPLETED shows only completed tasks`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Active", priority = Priority.HIGH))
        repository.insertTask(Task(title = "Done", priority = Priority.LOW, completed = true))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setFilter(TaskFilter.COMPLETED)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.tasks.size)
        assertTrue(state.tasks[0].completed)
    }

    @Test
    fun `toggleComplete flips completion status`() = runTest(testDispatcher) {
        val id = repository.insertTask(Task(title = "Toggle Me"))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        val task = repository.getTaskById(id)!!
        viewModel.toggleComplete(task)
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = repository.getTaskById(id)!!
        assertTrue(updated.completed)
    }

    @Test
    fun `search filters by title`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Buy groceries", priority = Priority.HIGH))
        repository.insertTask(Task(title = "Call mom", priority = Priority.LOW))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("groceries")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.tasks.size)
        assertEquals("Buy groceries", state.tasks[0].title)
    }

    @Test
    fun `search filters by description`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Task A", description = "water the plants"))
        repository.insertTask(Task(title = "Task B", description = "send email"))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("plants")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.tasks.size)
        assertEquals("Task A", state.tasks[0].title)
    }

    @Test
    fun `search is case insensitive`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Important Meeting"))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("IMPORTANT")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.tasks.size)
    }

    @Test
    fun `search and filter can be combined`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Buy milk", priority = Priority.HIGH))
        repository.insertTask(Task(title = "Buy eggs", priority = Priority.HIGH, completed = true))
        repository.insertTask(Task(title = "Call mom", priority = Priority.LOW))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setFilter(TaskFilter.PENDING)
        viewModel.setSearchQuery("buy")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.tasks.size)
        assertEquals("Buy milk", state.tasks[0].title)
    }

    @Test
    fun `clearing search restores all tasks`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Buy milk"))
        repository.insertTask(Task(title = "Call mom"))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("milk")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (viewModel.uiState.value as HomeUiState.Success).tasks.size)

        viewModel.setSearchQuery("")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, (viewModel.uiState.value as HomeUiState.Success).tasks.size)
    }

    @Test
    fun `sort by priority puts high first`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Low task", priority = Priority.LOW))
        repository.insertTask(Task(title = "High task", priority = Priority.HIGH))
        repository.insertTask(Task(title = "Medium task", priority = Priority.MEDIUM))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSort(TaskSort.PRIORITY_HIGH_FIRST)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(3, state.tasks.size)
        assertEquals(Priority.HIGH, state.tasks[0].priority)
        assertEquals(Priority.MEDIUM, state.tasks[1].priority)
        assertEquals(Priority.LOW, state.tasks[2].priority)
    }

    @Test
    fun `sort by due date puts earliest first`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Task B", dueDate = 2000L))
        repository.insertTask(Task(title = "Task A", dueDate = 1000L))
        repository.insertTask(Task(title = "Task C", dueDate = 3000L))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSort(TaskSort.DUE_DATE)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals("Task A", state.tasks[0].title)
        assertEquals("Task B", state.tasks[1].title)
        assertEquals("Task C", state.tasks[2].title)
    }

    @Test
    fun `sort by oldest puts oldest first`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "New", createdAt = 2000L))
        repository.insertTask(Task(title = "Old", createdAt = 1000L))
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSort(TaskSort.OLDEST)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals("Old", state.tasks[0].title)
        assertEquals("New", state.tasks[1].title)
    }

    @Test
    fun `undoDelete re-inserts task`() = runTest(testDispatcher) {
        val task = Task(title = "Deleted task", priority = Priority.HIGH)
        viewModel.startObserving()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.undoDelete(task)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, state.tasks.size)
        assertEquals("Deleted task", state.tasks[0].title)
    }
}
