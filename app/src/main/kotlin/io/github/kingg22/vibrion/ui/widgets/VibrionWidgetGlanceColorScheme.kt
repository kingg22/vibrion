package io.github.kingg22.vibrion.ui.widgets

import androidx.glance.material3.ColorProviders
import io.github.kingg22.vibrion.ui.theme.highContrastDarkColorScheme
import io.github.kingg22.vibrion.ui.theme.highContrastLightColorScheme

object VibrionWidgetGlanceColorScheme {
    val colors = ColorProviders(
        light = highContrastLightColorScheme,
        dark = highContrastDarkColorScheme,
    )
}
