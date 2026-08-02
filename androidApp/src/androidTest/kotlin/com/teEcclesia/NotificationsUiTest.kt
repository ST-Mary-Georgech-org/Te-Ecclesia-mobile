package com.teEcclesia

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class NotificationsUiTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNotifications_sadPath() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        // TODO: Navigate to Notifications when unauthenticated
        assert(true)
    }

    @Test
    fun testNotifications_happyPath() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        // TODO: Login, navigate to Notifications, verify list loads
        assert(true)
    }
}
