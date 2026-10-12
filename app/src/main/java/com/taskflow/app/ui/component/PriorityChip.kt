package com.taskflow.app.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskflow.app.data.model.Priority
import com.taskflow.app.ui.theme.PriorityHigh
import com.taskflow.app.ui.theme.PriorityLow
import com.taskflow.app.ui.theme.PriorityMedium

/**
 * Priority chip — pill badge dengan warna + ikon + teks.
 * Mengikuti DESIGN.md: setiap priority level punya glyph + teks, tidak hanya warna.
 * Warna diambil dari token tema (theme/Color.kt), bukan hex literal —
 * supaya konsisten di light & dark mode.
 */
@Composable
fun PriorityChip(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (color, icon, label) = when (priority) {
        Priority.HIGH -> Triple(PriorityHigh, Icons.Filled.PriorityHigh, "P0 - High")
        Priority.MEDIUM -> Triple(PriorityMedium, Icons.Filled.Remove, "P1 - Medium")
        Priority.LOW -> Triple(PriorityLow, Icons.Filled.KeyboardArrowDown, "P2 - Low")
    }

    FilterChip(
        selected = false,
        onClick = {},
        enabled = false,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color
            )
        },
        modifier = modifier
    )
}
