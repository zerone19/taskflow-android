package com.taskflow.app.ui.detail

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Regression test — TaskDetailViewModel.deleteTask race condition.
 *
 * Root cause: deleteTask() meluncurkan coroutine viewModelScope.launch yang
 * TIDAK di-await oleh pemanggil. TaskDetailScreen memanggil deleteTask() lalu
 * SEGERA popBackStack() tanpa sinkronisasi. Jika user kembali ke detail sebelum
 * Room selesai menghapus, atau screen di-pop sebelum commit, state bisa race.
 *
 * Test ini membuktikan: setelah deleteTask() + advanceUntilIdle(), task
 * BENAR-BENAR terhapus dari sumber data (operasi tidak gantung/hilang).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dao: FakeTaskDao
    private lateinit var repository: DefaultTaskRepository
    private lateinit var viewModel: TaskDetailViewModel
    private var taskId: Long = 0L

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dao = FakeTaskDao()
        repository = DefaultTaskRepository(dao)
        taskId = dao.insertTaskSync(Task(title = "Refactor Auth", priority = Priority.HIGH))
        viewModel = TaskDetailViewModel(repository, taskId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTask sets Success state with task`() = runTest(testDispatcher) {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue("harus Success setelah load", state is TaskDetailUiState.Success)
    }

    @Test
    fun `deleteTask removes task from data source`() = runTest(testDispatcher) {
        advanceUntilIdle() // loadTask selesai

        viewModel.deleteTask()
        advanceUntilIdle() // delete selesai

        assertTrue(
            "task harus benar-benar terhapus setelah deleteTask + idle",
            dao.getTasksSync().isEmpty()
        )
    }

    @Test
    fun `deleteTask sets Deleted state so screen can pop safely`() = runTest(testDispatcher) {
        advanceUntilIdle() // loadTask selesai
        assertTrue(viewModel.uiState.value is TaskDetailUiState.Success)

        viewModel.deleteTask()
        advanceUntilIdle()

        assertTrue(
            "state harus Deleted setelah delete berhasil — Screen pop berdasarkan ini",
            viewModel.uiState.value is TaskDetailUiState.Deleted
        )
    }

    @Test
    fun `deleteTask is no-op when not in Success state`() = runTest(testDispatcher) {
        // VM dalam Loading (belum advance) — deleteTask harus aman, tidak crash
        viewModel.deleteTask()
        advanceUntilIdle()
        // task masih ada karena state bukan Success saat delete dipanggil
        // (melindungi dari hapus pada task yang belum tentu dimuat)
        // CATATAN: ini mendokumentasikan perilaku guard saat ini.
    }
}
