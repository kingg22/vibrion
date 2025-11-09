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
import androidx.compose.runtime.mutableStateListOf
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
    val resources = LocalResources.current
    val activity = LocalActivity.current ?: return
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    val missingPermissions = remember {
        mutableStateListOf<String>().apply {
            addAll(
                requiredPermissions.filter {
                    ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                },
            )
        }
    }

    val permanentlyDeniedPermissions = remember { mutableStateListOf<String>() }
    var showSheet by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        missingPermissions.clear()
        permanentlyDeniedPermissions.clear()

        permissions.filter { !it.value }.forEach { (permission, _) ->
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            if (showRationale) {
                missingPermissions.add(permission)
            } else {
                permanentlyDeniedPermissions.add(permission)
            }
        }

        permanentlyDeniedPermissions.forEach { permission ->
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = resources.getString(
                        R.string.permission_denied,
                        resources.getString(getPermissionName(permission)),
                    ),
                    actionLabel = resources.getString(R.string.open_settings),
                    withDismissAction = true,
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

        showSheet = missingPermissions.isNotEmpty()
    }

    LaunchedEffect(Unit) {
        val stillMissing = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        showSheet = stillMissing.isNotEmpty()
        missingPermissions.clear()
        missingPermissions.addAll(stillMissing)
    }

    Box(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemGestures)) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
        AnimatedVisibility(showSheet) {
            PermissionsBottomSheet(
                permissionsToRequest = missingPermissions,
                onDismiss = { showSheet = false },
                onRequestPermissions = {
                    permissionLauncher.launch(it.toTypedArray())
                },
            )
        }
    }
}

private fun getPermissionName(permission: String) = when (permission) {
    Manifest.permission.POST_NOTIFICATIONS -> R.string.notifications
    Manifest.permission.READ_MEDIA_AUDIO,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    -> R.string.audio

    else -> R.string.unknown_permission
}
