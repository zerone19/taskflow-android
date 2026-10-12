package com.taskflow.app.data.repository

import com.taskflow.app.data.local.testing.FakeTaskDao
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DefaultTaskRepositoryTest {

    private lateinit var repository: DefaultTaskRepository
    private lateinit var fakeDao: FakeTaskDao

    @Before
    fun setup() {
        fakeDao = FakeTaskDao()
        repository = DefaultTaskRepository(fakeDao)
    }

    @Test
    fun `insertTask returns generated id`() = runTest {
        val task = Task(title = "Test Task", priority = Priority.HIGH)
        val id = repository.insertTask(task)
        assertTrue(id > 0)
    }

    @Test
    fun `getAllTasks returns inserted tasks`() = runTest {
        repository.insertTask(Task(title = "Task 1"))
        repository.insertTask(Task(title = "Task 2"))
        val tasks = repository.getAllTasks().first()
        assertEquals(2, tasks.size)
    }

    @Test
    fun `getTaskById returns correct task`() = runTest {
        val id = repository.insertTask(Task(title = "Find Me", priority = Priority.LOW))
        val task = repository.getTaskById(id)
        assertEquals("Find Me", task?.title)
        assertEquals(Priority.LOW, task?.priority)
    }

    @Test
    fun `getTaskById returns null for missing id`() = runTest {
        val task = repository.getTaskById(999L)
        assertNull(task)
    }

    @Test
    fun `updateTask modifies existing task`() = runTest {
        val id = repository.insertTask(Task(title = "Original"))
        val task = repository.getTaskById(id)!!
        repository.updateTask(task.copy(title = "Updated", completed = true))
        val updated = repository.getTaskById(id)!!
        assertEquals("Updated", updated.title)
        assertTrue(updated.completed)
    }

    @Test
    fun `deleteTask removes task`() = runTest {
        val id = repository.insertTask(Task(title = "To Delete"))
        val task = repository.getTaskById(id)!!
        repository.deleteTask(task)
        assertNull(repository.getTaskById(id))
    }
}
