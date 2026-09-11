package com.teEcclesia.designsystem.components.scanner

import androidx.compose.runtime.Composable
import dev.icerock.moko.permissions.PermissionsController

@Composable
expect fun PermissionsBindEffect(controller: PermissionsController)
