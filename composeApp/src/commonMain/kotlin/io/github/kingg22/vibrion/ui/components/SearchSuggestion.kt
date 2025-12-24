package io.github.kingg22.vibrion.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.clear_history
import io.github.kingg22.vibrion.domain.model.SearchHistoryItem
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchSuggestions(
    query: String,
    history: List<SearchHistoryItem>,
    onSuggestionClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBarPadding: Dp = 0.dp,
) {
    val historyQueries = history.map { it.query }
    val suggestions by remember(query, historyQueries) {
        derivedStateOf {
            if (query.isNotBlank()) {
                historyQueries.filter { it.contains(query, true) }
            } else {
                historyQueries
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = bottomBarPadding),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(suggestions, key = { it }) { suggestion ->
            DropdownMenuItem(
                text = { Text(suggestion) },
                leadingIcon = {
                    Icon(Icons.Default.History, null)
                },
                onClick = {
                    onSuggestionClick(suggestion)
                },
            )
        }

        if (history.isNotEmpty()) {
            item {
                TextButton(onClick = onClearHistory, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(Res.string.clear_history))
                }
            }
        }
    }
}

@Composable
@Preview
private fun SearchSuggestionPreview() {
    VibrionAppTheme(false) { SearchSuggestions("Test", emptyList(), {}, {}) }
}
