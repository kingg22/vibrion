package io.github.kingg22.vibrion.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import io.github.kingg22.vibrion.R
import androidx.glance.unit.ColorProvider as TextColorProvider

@SuppressLint("RestrictedApi") // false-positive, I use overload function
@Composable
fun MusicWidgetContent(
    modifier: GlanceModifier = GlanceModifier,
    title: String = "Track Title",
    artist: String = "Artist",
    isActive: Boolean = false,
) {
    // Context is required to get string from resources because glance don't support stringResource
    val context = LocalContext.current
    val playPauseAction = actionRunCallback<PlayPauseAction>()
    // TODO receive data of worker and action to worker

    Row(
        modifier.fillMaxSize().padding(16.dp).background(GlanceTheme.colors.background).cornerRadius(16.dp),
    ) {
        // Image of the music
        Column(GlanceModifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                provider = ImageProvider(android.R.drawable.screen_background_light),
                contentDescription = context.applicationContext.resources.getString(R.string.cover_of, title),
                modifier = GlanceModifier.size(85.dp).cornerRadius(10.dp),
            )
        }

        Column(GlanceModifier.fillMaxWidth()) {
            // Title and artist
            Row(GlanceModifier.fillMaxWidth()) {
                Column(GlanceModifier.padding(start = 12.dp, top = 5.dp)) {
                    Text(
                        title,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = ColorProvider(day = Color.Black, night = Color.White),
                        ),
                        maxLines = 1,
                    )
                    Text(
                        artist,
                        style = TextStyle(fontSize = 14.sp, color = TextColorProvider(color = Color.Gray)),
                        maxLines = 1,
                    )
                }
            }

            // Controls
            Row(
                GlanceModifier.fillMaxWidth().padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleIconButton(
                    onClick = {},
                    imageProvider = ImageProvider(R.drawable.baseline_shuffle_24),
                    contentDescription = context.applicationContext.resources.getString(R.string.shuffle),
                    modifier = GlanceModifier.defaultWeight(),
                )
                CircleIconButton(
                    onClick = {},
                    imageProvider = ImageProvider(R.drawable.baseline_skip_previous_24),
                    contentDescription = context.applicationContext.resources.getString(R.string.previous),
                    modifier = GlanceModifier.defaultWeight(),
                )
                if (isActive) {
                    CircleIconButton(
                        onClick = playPauseAction,
                        imageProvider = ImageProvider(R.drawable.baseline_pause_24),
                        contentDescription = context.applicationContext.resources.getString(R.string.pause),
                        modifier = GlanceModifier.defaultWeight(),
                    )
                } else {
                    CircleIconButton(
                        onClick = playPauseAction,
                        imageProvider = ImageProvider(R.drawable.baseline_play_arrow_24),
                        contentDescription = context.applicationContext.resources.getString(R.string.play),
                        modifier = GlanceModifier.defaultWeight(),
                    )
                }
                CircleIconButton(
                    onClick = {},
                    imageProvider = ImageProvider(R.drawable.baseline_skip_next_24),
                    contentDescription = context.applicationContext.resources.getString(R.string.next),
                    modifier = GlanceModifier.defaultWeight(),
                )
                CircleIconButton(
                    onClick = {},
                    imageProvider = ImageProvider(R.drawable.baseline_queue_music_24),
                    contentDescription = context.applicationContext.resources.getString(R.string.queue),
                    modifier = GlanceModifier.defaultWeight(),
                )
            }
        }
    }
}

@Suppress("unused")
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview
@Composable
private fun MusicWidgetContentPreview() {
    GlanceTheme(colors = VibrionWidgetGlanceColorScheme.colors) {
        MusicWidgetContent()
    }
}
