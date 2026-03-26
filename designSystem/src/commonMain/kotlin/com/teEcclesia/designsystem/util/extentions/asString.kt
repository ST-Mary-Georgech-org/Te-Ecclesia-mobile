package com.teEcclesia.designsystem.util.extentions

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringResource.asString(): String {
    return stringResource(this)
}