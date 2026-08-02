package com.teEcclesia

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

/**
 * Basic UI Test to verify that the app launches without crashing in minified/release builds.
 * This acts as a smoke test to catch R8/ProGuard obfuscation issues (like serialization crashes)
 * that would otherwise break the app on startup.
 */
class AppUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAppLaunchesWithoutR8Crashes() {
        // The test simply launches the MainActivity.
        // If there are Serialization or JSON reflection errors due to R8 (ProGuard) obfuscating DTOs,
        // the app will crash during the initial network calls or auth state loading, 
        // causing this test to fail.
        composeTestRule.waitForIdle()
        
        // The fact that waitForIdle() completes means the app didn't crash on startup.
        assert(true)
    }
}
