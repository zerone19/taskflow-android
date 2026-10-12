package com.taskflow.app.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Helper tema untuk UI test — render konten dengan TaskFlowTheme
 * supaya token warna/tipografi tersedia saat test.
 */
@Composable
fun TaskFlowThemeForTest(content: @Composable () -> Unit) {
    MaterialTheme(content = { content() })
}
