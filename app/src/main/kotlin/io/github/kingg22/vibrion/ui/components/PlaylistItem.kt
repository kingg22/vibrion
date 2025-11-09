package io.github.kingg22.vibrion.ui.components

import dev.drewhamilton.poko.Poko

@Poko
class PlaylistItem(
    val image: Any,
    val headline: String,
    val description: String,
    val contentDescription: String? = null,
)
