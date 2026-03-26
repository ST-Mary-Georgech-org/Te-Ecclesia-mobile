package com.teEcclesia.home.presentation.api

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.home.api.HomeFeatureApi
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.home

class HomeFeatureApiImpl : HomeFeatureApi {

    @Composable
    override fun TabEntry(updateBottomNavigationVisibility: (Boolean) -> Unit) {
        Box(
            Modifier.fillMaxSize().background(Color.Yellow),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Column {
                Text(Res.string.home.asString(), Theme.typography.label.medium.medium)
                Text(
                    "text",
                    Theme.typography.label.medium.medium,
                    modifier = Modifier.clickable {

                    })
            }
        }
    }
}