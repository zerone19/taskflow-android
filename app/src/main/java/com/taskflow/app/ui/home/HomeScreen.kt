package com.taskflow.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.taskflow.app.R
import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.ui.component.EmptyState
import com.taskflow.app.ui.component.FilterChips
import com.taskflow.app.ui.component.TaskCard
import com.taskflow.app.ui.theme.SpaceMD
import com.taskflow.app.ui.theme.SpaceSM

/**
 * HomeScreen — stateless composable.
 * Menampilkan task list, empty state, filter chips, FAB.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    taskRepository: TaskRepository,
    onTaskClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    deletedTask: com.taskflow.app.data.model.Task? = null,
    onUndoConsumed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(taskRepository)
    )
    // Panggil dari LaunchedEffect, BUKAN langsung di body composable —
    // body composable dieksekusi ulang setiap recomposition.
    LaunchedEffect(Unit) {
        viewModel.startObserving()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Reset search saat kembali ke Home (navigasi dari detail/form).
    // isSearching & searchQuery adalah state lokal — reset di sini supaya
    // user tidak melihat hasil search lama yang membingungkan.
    LaunchedEffect(Unit) {
        isSearching = false
        searchQuery = ""
        viewModel.setSearchQuery("")
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // Tampilkan Undo Snackbar ketika ada task yang baru dihapus.
    LaunchedEffect(deletedTask) {
        deletedTask?.let { task ->
            val result = snackbarHostState.showSnackbar(
                message = "Task deleted",
                actionLabel = "UNDO",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete(task)
            }
            onUndoConsumed()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (isSearching) {
                TextField(
                    value = searchQuery,
                    onValueChange = { q ->
                        searchQuery = q
                        viewModel.setSearchQuery(q)
                    },
                    placeholder = { Text("Search tasks...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchQuery.isNotEmpty()) {
                                searchQuery = ""
                                viewModel.setSearchQuery("")
                            } else {
                                isSearching = false
                                viewModel.setSearchQuery("")
                            }
                        }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ic_taskflow_logo),
                                contentDescription = "TaskFlow logo",
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(SpaceSM))
                            Text("TaskFlow")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search tasks")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add Task") },
                text = { Text("New Task") }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is HomeUiState.Empty -> {
                EmptyState(
                    onCreateClick = onAddTaskClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is HomeUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is HomeUiState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    FilterChips(
                        selectedFilter = state.filter,
                        onFilterSelected = viewModel::setFilter
                    )
                    // Sort chips
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = SpaceMD),
                        horizontalArrangement = Arrangement.spacedBy(SpaceSM)
                    ) {
                        items(TaskSort.entries.toList()) { sort ->
                            FilterChip(
                                selected = sort == state.sort,
                                onClick = { viewModel.setSort(sort) },
                                label = { Text(sort.label) }
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(SpaceMD),
                        verticalArrangement = Arrangement.spacedBy(SpaceSM)
                    ) {
                        items(state.tasks, key = { it.id }) { task ->
                            TaskCard(
                                task = task,
                                onToggleComplete = { viewModel.toggleComplete(it) },
                                onClick = { onTaskClick(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private val TaskSort.label: String
    get() = when (this) {
        TaskSort.NEWEST -> "Newest"
        TaskSort.OLDEST -> "Oldest"
        TaskSort.PRIORITY_HIGH_FIRST -> "Priority"
        TaskSort.DUE_DATE -> "Due Date"
    }
