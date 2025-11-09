package io.github.kingg22.vibrion.ui.components

import dev.drewhamilton.poko.Poko

@Poko
class CarouselItem(
    val id: String,
    val position: Int,
    val painter: Any,
    val contentDescription: String? = null,
    val artistName: String,
    val titleSong: String,
)
