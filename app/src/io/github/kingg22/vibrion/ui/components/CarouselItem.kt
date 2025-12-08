package io.github.kingg22.vibrion.ui.components

data class CarouselItem(
    val id: String,
    val position: Int,
    val painter: Any,
    val contentDescription: String? = null,
    val artistName: String,
    val titleSong: String,
)
