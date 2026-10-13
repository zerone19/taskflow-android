package com.taskflow.app.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taskflow.app.ui.home.TaskFilter
import com.taskflow.app.ui.theme.SpaceMD
import com.taskflow.app.ui.theme.SpaceSM

/**
 * FilterChips — horizontal scrollable filter chips untuk HomeScreen.
 * Menampilkan semua opsi TaskFilter dengan highlight pada yang aktif.
 */
@Composable
fun FilterChips(
    selectedFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = SpaceMD),
        horizontalArrangement = Arrangement.spacedBy(SpaceSM)
    ) {
        items(TaskFilter.entries.toList()) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

private val TaskFilter.label: String
    get() = when (this) {
        TaskFilter.ALL -> "All"
        TaskFilter.PENDING -> "Pending"
        TaskFilter.HIGH_PRIORITY -> "High Priority"
        TaskFilter.COMPLETED -> "Completed"
    }
