package io.github.kingg22.vibrion.app.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.R
import io.github.kingg22.vibrion.app.ui.screens.permissions.PermissionsHandlerScreen
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun SettingsContent(token: String, updateToken: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf(token) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.deezer_name),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        updateToken(it)
                    },
                    label = { Text(stringResource(R.string.token)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    singleLine = true,
                    visualTransformation =
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions =
                    if (passwordVisible) {
                        KeyboardOptions()
                    } else {
                        KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                        )
                    },
                    trailingIcon = {
                        val image: ImageVector
                        val description: String
                        if (passwordVisible) {
                            image = Icons.Filled.Visibility
                            description = stringResource(R.string.hide) + " " + stringResource(R.string.token)
                        } else {
                            image = Icons.Filled.VisibilityOff
                            description = stringResource(R.string.show) + " " + stringResource(R.string.token)
                        }

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                )
            }
            PermissionsHandlerScreen(snackBarHostState)
        }
    }
}

@PreviewScreenSizes
@Composable
private fun SettingsPreview() {
    VibrionAppTheme {
        SettingsContent("", {})
    }
}
