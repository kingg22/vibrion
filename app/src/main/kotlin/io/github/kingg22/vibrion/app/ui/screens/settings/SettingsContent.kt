package io.github.kingg22.vibrion.app.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.R
import io.github.kingg22.vibrion.app.ui.screens.permissions.PermissionsHandlerScreen
import io.github.kingg22.vibrion.app.ui.theme.VibrionAppTheme

@Composable
fun SettingsContent(
    token: String,
    updateToken: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf(token) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.back))
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text =
                    stringResource(R.string.deezer_arl),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                )
                Text(
                    buildAnnotatedString {
                        withLink(
                            LinkAnnotation.Url(
                                "https://www.dumpmedia.com/deezplus/deezer-arl.html",
                                TextLinkStyles(
                                    SpanStyle(color = Color.Cyan, textDecoration = TextDecoration.Underline),
                                ),
                            ),
                        ) {
                            append(stringResource(R.string.arl_tutorial))
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        updateToken(it)
                    },
                    label = { Text(stringResource(R.string.token)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    singleLine = true,
                    visualTransformation =
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = if (passwordVisible) {
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
                            description = stringResource(R.string.hide)
                        } else {
                            image = Icons.Filled.VisibilityOff
                            description = stringResource(R.string.show)
                        }

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, "$description ${stringResource(R.string.token)}")
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
        SettingsContent("", {}, {})
    }
}
