package io.github.kingg22.vibrion.ui.screens.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.github.kingg22.vibrion.R
import kotlinx.coroutines.launch

@Composable
fun PermissionsHandlerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: return
    val resources = LocalResources.current
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    // Solo se pide esta si es necesaria.
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null
    }

    // Estado simple: ¿faltan permisos?
    var shouldShowSheet by rememberSaveable { mutableStateOf(false) }

    // Launcher simplificado (solo 1 permiso)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            shouldShowSheet = false
            return@rememberLauncherForActivityResult
        }

        val permission = notificationPermission ?: return@rememberLauncherForActivityResult

        val canRequestAgain = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

        if (!canRequestAgain) {
            // Usuario lo negó permanentemente
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = resources.getString(
                        R.string.permission_denied,
                        resources.getString(R.string.notifications),
                    ),
                    actionLabel = resources.getString(R.string.open_settings),
                    duration = SnackbarDuration.Long,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        },
                    )
                }
            }
        }

        // Si puede solicitarse nuevamente, o incluso si no, mantenemos bottom sheet abierto
        shouldShowSheet = true
    }

    // Primera evaluación al entrar
    LaunchedEffect(Unit) {
        notificationPermission?.let { perm ->
            val missing = ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED
            shouldShowSheet = missing
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemGestures),
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )

        AnimatedVisibility(visible = shouldShowSheet && notificationPermission != null) {
            PermissionsBottomSheet(
                onDismiss = { shouldShowSheet = false },
                onRequestPermissions = {
                    notificationPermission?.let { p -> permissionLauncher.launch(p) }
                },
            )
        }
    }
}
