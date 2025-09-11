package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.R

@Composable
@Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
fun ResultTopBar(query: String, onBackClick: () -> Unit, onSearch: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf(query) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    DockedSearchBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.Companion.statusBars)
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp),
        expanded = expanded,
        onExpandedChange = { expanded = it },
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
                    IconButton(onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
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
                modifier = Modifier.Companion.fillMaxWidth(),
            )
        },
        content = {},
    )
}
