package io.github.kingg22.vibrion.ui.widgets

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class PlayPauseAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        // TODO Aquí se manejaría la lógica para pausar o reanudar la música
    }
}
