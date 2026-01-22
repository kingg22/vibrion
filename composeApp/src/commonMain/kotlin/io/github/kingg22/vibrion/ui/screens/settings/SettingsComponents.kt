package io.github.kingg22.vibrion.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.Icons
import io.github.kingg22.vibrion.Res
import io.github.kingg22.vibrion.accounts
import io.github.kingg22.vibrion.deezer_name
import io.github.kingg22.vibrion.filled.Visibility
import io.github.kingg22.vibrion.filled.VisibilityOff
import io.github.kingg22.vibrion.hide
import io.github.kingg22.vibrion.outlined.Handshake
import io.github.kingg22.vibrion.show
import io.github.kingg22.vibrion.token
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsSection(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp),
        )
        content()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsAccountSection(token: String?, updateToken: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf(token) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    SettingsSection(stringResource(Res.string.accounts)) {
        Column(modifier = modifier.padding(horizontal = 16.dp)) {
            Text(
                text = stringResource(Res.string.deezer_name),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
            OutlinedTextField(
                value = text ?: "",
                onValueChange = {
                    text = it
                    updateToken(it)
                },
                label = { Text(stringResource(Res.string.token)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                singleLine = true,
                visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions =
                if (passwordVisible) KeyboardOptions() else KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) {
                        stringResource(Res.string.hide) + " " + stringResource(Res.string.token)
                    } else {
                        stringResource(Res.string.show) + " " + stringResource(Res.string.token)
                    }

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
            )
        }
    }
}

@Composable
fun AppVersion(versionText: String, copyrights: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(onClick, modifier, shape = MaterialTheme.shapes.medium) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            Box(Modifier.size(30.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    versionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.44f),
                )
                Text(
                    copyrights,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.44f),
                )
            }
        }
    }
}

@Composable
fun CategoryItem(title: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick,
        modifier,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview
@Composable
private fun SettingsSectionPreview() {
    VibrionAppTheme {
        SettingsSection(title = "Sample Section") {
            Text("Sample content")
        }
    }
}

@Preview
@Composable
private fun SettingsAccountSectionPreview() {
    VibrionAppTheme {
        SettingsAccountSection("Sample Section", {})
    }
}

@Preview
@Composable
private fun AppVersionPreview() {
    VibrionAppTheme {
        AppVersion("Sample Version", "Sample Copyright", {})
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    VibrionAppTheme {
        CategoryItem("Sample Category", Icons.Outlined.Handshake, {})
    }
}
