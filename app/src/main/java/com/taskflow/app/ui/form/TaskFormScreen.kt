package com.taskflow.app.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.taskflow.app.data.model.Priority
import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.ui.theme.MarginMobile
import com.taskflow.app.ui.theme.SpaceLG
import com.taskflow.app.ui.theme.SpaceMD
import com.taskflow.app.ui.theme.SpaceSM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    taskRepository: TaskRepository,
    taskId: Long? = null,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TaskFormViewModel = viewModel(
        factory = TaskFormViewModelFactory(taskRepository, taskId)
    )
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = formState.dueDate
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDueDateChange(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(if (taskId != null) "Edit Task" else "New Task") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MarginMobile)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            OutlinedTextField(
                value = formState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title *") },
                isError = formState.titleError != null,
                supportingText = formState.titleError?.let { error -> { Text(error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(SpaceSM))

            OutlinedTextField(
                value = formState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(SpaceMD))

            Text(
                text = "Priority",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(SpaceSM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceSM)
            ) {
                Priority.entries.forEach { priority ->
                    FilterChip(
                        selected = formState.priority == priority,
                        onClick = { viewModel.onPriorityChange(priority) },
                        label = { Text(priority.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpaceMD))

            Text(
                text = "Due Date",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(SpaceSM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceSM),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = { showDatePicker = true }) {
                    Text(
                        formState.dueDate?.let { millis ->
                            val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            fmt.format(Date(millis))
                        } ?: "Set due date"
                    )
                }
                if (formState.dueDate != null) {
                    OutlinedButton(onClick = { viewModel.onDueDateChange(null) }) {
                        Text("Clear")
                    }
                }
            }

            Spacer(modifier = Modifier.height(SpaceMD))

            Text(
                text = "Tags",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(SpaceSM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceSM)
            ) {
                listOf("Work", "Personal", "Urgent", "Ideas").forEach { tag ->
                    FilterChip(
                        selected = tag in formState.tags,
                        onClick = { viewModel.onTagToggle(tag) },
                        label = { Text(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpaceLG))

            formState.saveError?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(SpaceSM))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceSM)
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = { viewModel.save(onSaveClick) },
                    modifier = Modifier.weight(1f),
                    enabled = !formState.isSaving
                ) {
                    Text(if (formState.isSaving) "Saving..." else "Save")
                }
            }
        }
    }
}
