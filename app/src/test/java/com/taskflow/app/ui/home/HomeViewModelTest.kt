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
}
