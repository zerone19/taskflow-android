package com.taskflow.app.ui.home

import com.taskflow.app.data.local.testing.CountingTaskDao
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
import org.junit.Before
import org.junit.Test

/**
 * Regression test untuk TEST 06 — Bug: startObserving() tidak idempotent.
 *
 * Root cause: HomeScreen memanggil viewModel.startObserving() langsung di
 * body composable, sehingga setiap recomposition memanggil loadTasks()
 * lagi dan membuat collector Flow baru yang menumpuk di repository.
 * Fix yang diharapkan: startObserving() harus no-op jika sudah observing,
 * dan HomeScreen harus memanggilnya dari LaunchedEffect(Unit).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelObserveTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var countingDao: CountingTaskDao
    private lateinit var repository: DefaultTaskRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        countingDao = CountingTaskDao()
        repository = DefaultTaskRepository(countingDao)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startObserving called repeatedly keeps exactly one subscription`() = runTest(testDispatcher) {
        // Simulasi recomposition: HomeScreen memanggil startObserving()
        // beberapa kali (body composable dieksekusi ulang).
        viewModel.startObserving()
        viewModel.startObserving()
        viewModel.startObserving()
        advanceUntilIdle()

        assertEquals(
            "startObserving() harus idempotent — tidak boleh membuat collector baru tiap panggilan",
            1,
            countingDao.subscriptionCount.value
        )
    }

    @Test
    fun `state stays correct after repeated startObserving calls`() = runTest(testDispatcher) {
        repository.insertTask(Task(title = "Task 1", priority = Priority.HIGH))
        repository.insertTask(Task(title = "Task 2"))

        viewModel.startObserving()
        advanceUntilIdle()
        viewModel.startObserving() // recomposition lagi
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is HomeUiState.Success) { "Expected Success but got $state" }
        assertEquals(
            "Task tidak boleh terduplikasi oleh multiple collectors",
            2,
            (state as HomeUiState.Success).tasks.size
        )
    }
}
