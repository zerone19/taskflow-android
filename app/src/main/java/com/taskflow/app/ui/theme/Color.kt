package com.taskflow.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// === Warna dari design/taskflow/DESIGN.md (frontmatter YAML) ===

// Primary — Electric Indigo
val ElectricIndigo = Color(0xFF3525CD)
val ElectricIndigoContainer = Color(0xFF4F46E5)
val OnElectricIndigoContainer = Color(0xFFDAD7FF)
val InversePrimary = Color(0xFFC3C0FF)

// Secondary — Teal
val Teal = Color(0xFF006A61)
val TealContainer = Color(0xFF86F2E4)
val OnTealContainer = Color(0xFF006F66)

// Tertiary
val SlateBlue = Color(0xFF004C76)
val OnSlateBlue = Color(0xFFFFFFFF)
val SlateBlueContainer = Color(0xFF00659A)
val OnSlateBlueContainer = Color(0xFFBEDFFF)

// Error
val CrimsonRed = Color(0xFFBA1A1A)
val OnCrimsonRed = Color(0xFFFFFFFF)
val CrimsonContainer = Color(0xFFFFDAD6)
val OnCrimsonContainer = Color(0xFF93000A)

// Surfaces
val WarmSlateBackground = Color(0xFFFAF8FF)
val PureWhite = Color(0xFFFFFFFF)
val OnWarmSlate = Color(0xFF131B2E)
val OnWarmSlateVariant = Color(0xFF464555)
val InverseSurface = Color(0xFF283044)
val InverseOnSurface = Color(0xFFEEF0FF)
val OutlineGrey = Color(0xFF777587)
val OutlineVariantGrey = Color(0xFFC7C4D8)
val SurfaceTintIndigo = Color(0xFF4D44E3)

// Surface containers (light mode)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF2F3FF)
val SurfaceContainer = Color(0xFFEAEDFF)
val SurfaceContainerHigh = Color(0xFFE2E7FF)
val SurfaceContainerHighest = Color(0xFFDAE2FD)
val SurfaceVariantWarm = Color(0xFFDAE2FD)

// Dark mode surfaces (dari DESIGN.md section Surface Elevation System)
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceContainerLow = Color(0xFF161F30)
val DarkSurfaceContainer = Color(0xFF243248)
val DarkSurfaceContainerHigh = Color(0xFF334155)
val DarkOnSurface = Color(0xFFE2E8F0)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)

// Priority semantic colors
val PriorityHigh = Color(0xFFEF4444)
val PriorityMedium = Color(0xFFF59E0B)
val PriorityLow = Color(0xFF10B981)

private val LightColors = lightColorScheme(
    primary = ElectricIndigo,
    onPrimary = PureWhite,
    primaryContainer = ElectricIndigoContainer,
    onPrimaryContainer = OnElectricIndigoContainer,
    inversePrimary = InversePrimary,
    secondary = Teal,
    onSecondary = PureWhite,
    secondaryContainer = TealContainer,
    onSecondaryContainer = OnTealContainer,
    tertiary = SlateBlue,
    onTertiary = OnSlateBlue,
    tertiaryContainer = SlateBlueContainer,
    onTertiaryContainer = OnSlateBlueContainer,
    error = CrimsonRed,
    onError = OnCrimsonRed,
    errorContainer = CrimsonContainer,
    onErrorContainer = OnCrimsonContainer,
    background = WarmSlateBackground,
    onBackground = OnWarmSlate,
    surface = PureWhite,
    onSurface = OnWarmSlate,
    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = OnWarmSlateVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    outline = OutlineGrey,
    outlineVariant = OutlineVariantGrey,
    surfaceTint = SurfaceTintIndigo,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)

private val DarkColors = darkColorScheme(
    primary = ElectricIndigo,
    onPrimary = PureWhite,
    primaryContainer = ElectricIndigoContainer,
    onPrimaryContainer = OnElectricIndigoContainer,
    inversePrimary = InversePrimary,
    secondary = Teal,
    onSecondary = PureWhite,
    secondaryContainer = TealContainer,
    onSecondaryContainer = OnTealContainer,
    tertiary = SlateBlue,
    onTertiary = OnSlateBlue,
    tertiaryContainer = SlateBlueContainer,
    onTertiaryContainer = OnSlateBlueContainer,
    error = CrimsonRed,
    onError = OnCrimsonRed,
    errorContainer = CrimsonContainer,
    onErrorContainer = OnCrimsonContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceContainerHigh,
    onSurfaceVariant = DarkOnSurfaceVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    outline = DarkOnSurfaceVariant,
    outlineVariant = DarkSurfaceContainerHigh,
    surfaceTint = SurfaceTintIndigo,
    surfaceContainerLowest = DarkSurfaceContainerLow,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHigh
)

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = TaskFlowTypography,
        shapes = TaskFlowShapes,
        content = content
    )
}
