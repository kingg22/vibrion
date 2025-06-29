package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.kingg22.vibrion.app.R
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun SearchScreen(onSearch: (String) -> Unit, onSettingsClick: () -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = {
                            expanded = false
                            text = it
                            onSearch(it)
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text(stringResource(R.string.search_placeholder)) },
                        leadingIcon = {
                            IconButton(onSettingsClick) {
                                Icon(Icons.Outlined.Settings, stringResource(R.string.open_settings))
                            }
                        },
                        trailingIcon = {
                            IconButton({
                                if (text.isNotBlank()) {
                                    onSearch(text)
                                }
                            }) {
                                Icon(Icons.Default.Search, stringResource(R.string.search))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                content = {},
            )
        }
    }
}

@PreviewScreenSizes
@Preview(showSystemUi = true)
@Composable
private fun SearchPreview() {
    VibrionAppTheme {
        SearchScreen(onSearch = {}, onSettingsClick = {})
    }
}
