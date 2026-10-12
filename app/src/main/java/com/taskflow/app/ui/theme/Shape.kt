package com.taskflow.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// === Shape dari design/taskflow/DESIGN.md (Material 3 corner scale) ===

val TaskFlowShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),    // status indicator, badge kecil
    small = RoundedCornerShape(8.dp),          // text field, chip, dropdown
    medium = RoundedCornerShape(12.dp),        // ElevatedCard, bottom sheet
    large = RoundedCornerShape(24.dp),         // FAB, search bar, AlertDialog
    extraLarge = RoundedCornerShape(28.dp)     // AlertDialog (large shape)
)
