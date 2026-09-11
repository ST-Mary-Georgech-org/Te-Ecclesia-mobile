package com.teEcclesia.designsystem.components.scanner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrCodeScanner

@Composable
fun QrScanner(
    modifier: Modifier = Modifier,
    flashlightOn: Boolean = false,
    cameraLens: CameraLens = CameraLens.Back,
    onCompletion: (String) -> Unit,
    onFailure: (String) -> Unit = {},
    overlayShape: OverlayShape = OverlayShape.Square,
    overlayColor: Color = Color(0x88000000),
    overlayBorderColor: Color = Color.White,
    zoomLevel: Float = 1f,
    maxZoomLevel: Float = 3f,
    customOverlay: (ContentDrawScope.() -> Unit)? = null,
    permissionDeniedView: @Composable (((onOpenSettings: () -> Unit) -> Unit))? = null
) {
    val coroutineScope = rememberCoroutineScope()

    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    PermissionsBindEffect(controller)

    var permissionState by remember { mutableStateOf(PermissionState.NotDetermined) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, controller) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch {
                    permissionState = controller.getPermissionState(Permission.CAMERA)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(controller) {
        if (!controller.isPermissionGranted(Permission.CAMERA)) {
            try {
                controller.providePermission(Permission.CAMERA)
                permissionState = PermissionState.Granted
            } catch (_: Exception) {
                permissionState = controller.getPermissionState(Permission.CAMERA)
            }
        } else {
            permissionState = PermissionState.Granted
        }
    }

    if (permissionState == PermissionState.Granted) {
        QrCodeScanner(
            modifier = modifier,
            flashlightOn = flashlightOn,
            cameraLens = cameraLens,
            onCompletion = onCompletion,
            overlayShape = overlayShape,
            overlayColor = overlayColor,
            overlayBorderColor = overlayBorderColor,
            zoomLevel = zoomLevel,
            maxZoomLevel = maxZoomLevel,
            customOverlay = customOverlay,
            permissionDeniedView = {
                if (permissionDeniedView != null) {
                    permissionDeniedView { controller.openAppSettings() }
                }
            }
        )
    } else {
        if (permissionDeniedView != null) {
            permissionDeniedView { controller.openAppSettings() }
        } else {
            CameraPermissionDeniedContent(
                onOpenSettings = {
                    controller.openAppSettings()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
