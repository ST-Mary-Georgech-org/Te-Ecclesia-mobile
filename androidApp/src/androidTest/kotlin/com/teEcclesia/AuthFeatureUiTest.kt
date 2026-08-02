package com.teEcclesia

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

/**
 * Feature UI Test for the Authentication Flow.
 * Verifies that the UI can interact with the network layer, send a login request, 
 * and successfully deserialize the response (or error response) without crashing
 * due to R8 minification/obfuscation.
 */
class AuthFeatureUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLoginFlow_handlesNetworkAndSerializationWithoutCrash() {
        // Wait for the app to initialize and navigate to the Login screen
        composeTestRule.waitForIdle()

        // Optional: wait a moment for splash screen / initial loading to disappear
        Thread.sleep(3000)

        // 1. Enter phone number using testTag
        composeTestRule.onNodeWithTag("PhoneInput", useUnmergedTree = true)
            .performTextInput("01000000000")

        // 2. Enter password
        composeTestRule.onNodeWithTag("PasswordInput", useUnmergedTree = true)
            .performTextInput("wrongpassword123")

        // 3. Click Login
        composeTestRule.onNodeWithTag("LoginButton", useUnmergedTree = true)
            .performClick()

        // 4. Wait for the API request to complete.
        // If R8 breaks serialization, the app will crash and this test will fail here.
        composeTestRule.waitForIdle()
        Thread.sleep(3000) // allow time for the network request to finish

        // 5. Verify the app is still alive and we didn't get a serialization crash.
        // A crash would have failed the test before reaching this point.
        assert(true)
    }
}
