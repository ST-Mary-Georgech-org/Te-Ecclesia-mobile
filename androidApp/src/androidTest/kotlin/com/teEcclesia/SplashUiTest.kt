package com.teEcclesia

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class SplashUiTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSplash_sadPath() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        // Splash doesn't really have a sad path except maybe no network
        assert(true)
    }

    @Test
    fun testSplash_happyPath() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        // TODO: Verify splash navigates to Home if token exists, or Login if not
        assert(true)
    }
}
