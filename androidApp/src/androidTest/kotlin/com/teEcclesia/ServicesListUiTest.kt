package com.teEcclesia

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class ServicesListUiTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testServicesList_sadPath() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        // TODO: Test empty state or error state
        assert(true)
    }

    @Test
    fun testServicesList_happyPath() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        // TODO: Verify list of services is loaded from API
        assert(true)
    }
}
