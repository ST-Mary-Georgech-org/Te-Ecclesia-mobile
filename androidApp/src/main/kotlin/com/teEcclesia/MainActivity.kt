package com.teEcclesia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.teEcclesia.identity.domain.util.AppLocalizer
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val localizer: AppLocalizer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }
        FileKit.init(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        localizer.applyLocaleToContext()

        setContent {
            App()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.requestFocus()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
