package io.github.kingg22.vibrion.ui.widgets

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent

class MusicWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(colors = VibrionWidgetGlanceColorScheme.colors) {
                MusicWidgetContent()
            }
        }
    }
}
