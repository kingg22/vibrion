package io.github.kingg22.vibrion.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
class VibrionPage<out Key : Any, out Value : Any>(val data: List<Value>, val prevKey: Key?, val nextKey: Key?)
