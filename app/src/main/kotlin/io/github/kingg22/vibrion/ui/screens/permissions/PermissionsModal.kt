package io.github.kingg22.vibrion.ui.screens.permissions

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kingg22.vibrion.R
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PermissionsBottomSheet(onDismiss: () -> Unit, onRequestPermissions: () -> Unit, modifier: Modifier = Modifier) {
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

            Text(stringResource(R.string.notifications_permission), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(3.dp))

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onRequestPermissions,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.allow_permissions))
            }
        }
    }
}

@Preview
@Composable
private fun PermissionBottomSheetPreview() {
    VibrionAppTheme {
        PermissionsBottomSheet(onDismiss = {}, onRequestPermissions = {})
    }
}
