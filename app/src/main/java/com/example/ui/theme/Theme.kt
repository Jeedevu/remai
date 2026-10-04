package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RemLightColorScheme = lightColorScheme(
    primary = RemPrimary,
    onPrimary = RemOnPrimary,
    primaryContainer = RemPrimaryContainer,
    onPrimaryContainer = RemOnPrimaryContainer,
    secondary = RemSecondary,
    onSecondary = RemOnSecondary,
    secondaryContainer = RemSecondaryContainer,
    onSecondaryContainer = RemOnSecondaryContainer,
    tertiary = RemTertiary,
    onTertiary = RemOnTertiary,
    tertiaryContainer = RemTertiaryContainer,
    onTertiaryContainer = RemOnTertiaryContainer,
    error = RemError,
    onError = RemOnError,
    errorContainer = RemErrorContainer,
    onErrorContainer = RemOnErrorContainer,
    background = RemSurface,
    onBackground = RemOnSurface,
    surface = RemSurface,
    onSurface = RemOnSurface,
    surfaceVariant = RemSurfaceContainerHighest,
    onSurfaceVariant = RemOnSurfaceVariant,
    outline = RemOutline,
    outlineVariant = RemOutlineVariant,
    inverseSurface = RemInverseSurface,
    inverseOnSurface = RemInverseOnSurface
)

private val RemDarkColorScheme = darkColorScheme(
    primary = RemPrimaryContainer,
    onPrimary = RemOnPrimaryContainer,
    primaryContainer = RemPrimary,
    onPrimaryContainer = RemOnPrimary,
    secondary = RemSecondaryContainer,
    onSecondary = RemOnSecondary,
    secondaryContainer = RemSecondary,
    onSecondaryContainer = RemOnSecondaryContainer,
    tertiary = RemTertiaryContainer,
    onTertiary = RemOnTertiaryContainer,
    background = RemInverseSurface,
    onBackground = RemInverseOnSurface,
    surface = RemInverseSurface,
    onSurface = RemInverseOnSurface
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We intentionally preserve the rich graphic neo-brutalist palette defined in the design specs
    val colorScheme = if (darkTheme) RemDarkColorScheme else RemLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
