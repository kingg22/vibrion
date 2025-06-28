package io.github.kingg22.vibrion.app.ui.screens.permissions

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.app.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PermissionsBottomSheet(
    onDismiss: () -> Unit,
    permissionsToRequest: List<String>,
    onRequestPermissions: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
        ) {
            Text(stringResource(R.string.required_permissions), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.permissions_required), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))

            permissionsToRequest.forEach { permission ->
                val explanation = when (permission) {
                    Manifest.permission.POST_NOTIFICATIONS -> R.string.notifications_permission
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    -> R.string.audio_permission

                    else -> R.string.unknown_permission
                }
                Text(stringResource(explanation), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(3.dp))
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onRequestPermissions(permissionsToRequest) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.allow_permissions))
            }
        }
    }
}
