package com.teEcclesia.designsystem.components.scanner

import androidx.compose.runtime.Composable
import dev.icerock.moko.permissions.PermissionsController

@Composable
actual fun PermissionsBindEffect(controller: PermissionsController) {
    // No-op on iOS
}
