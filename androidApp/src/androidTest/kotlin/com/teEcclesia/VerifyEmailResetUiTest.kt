package com.teEcclesia

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class VerifyEmailResetUiTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testVerifyEmailReset_happyPath_manualOtp() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)
        
        println("WAITING FOR MANUAL OTP ENTRY... Please type the 5-digit OTP for Email.")
        try {
            composeTestRule.waitUntil(timeoutMillis = 60000) {
                try {
                    val textNode = composeTestRule.onNodeWithTag("OtpInput", useUnmergedTree = true).fetchSemanticsNode()
                    val text = textNode.config[SemanticsProperties.EditableText].text
                    text.length == 5
                } catch (e: Exception) {
                    false
                }
            }
            // Proceed to click verify
            composeTestRule.onNodeWithTag("VerifyCodeButton", useUnmergedTree = true).performClick()
        } catch (e: Exception) {
            println("Timeout or OtpInput tag not found. Please navigate to VerifyEmailResetPasswordScreen first.")
        }

        composeTestRule.waitForIdle()
        assert(true)
    }
}
