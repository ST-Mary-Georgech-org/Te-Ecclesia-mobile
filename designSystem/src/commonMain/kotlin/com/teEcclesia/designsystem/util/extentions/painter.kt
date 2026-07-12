package com.teEcclesia.designsystem.util.extentions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawableResource.painter(): Painter {
    return painterResource(this)
}
