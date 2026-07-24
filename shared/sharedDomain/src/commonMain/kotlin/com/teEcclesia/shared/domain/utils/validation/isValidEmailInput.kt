package com.teEcclesia.shared.domain.utils.validation

fun isValidEmailInput(email: String): Boolean {
    // 1. Block spaces
    if (email.contains(" ")) return false
    
    // 2. Prevent consecutive dots (e.g., "john..smith")
    if (email.contains("")) return false

    // 3. Progressive regex for typing state
    // - Local part: letters, numbers, and allowed symbols (. _ - +)
    // - Optional @ symbol followed by optional domain and optional TLD
    val typingRegex = "^[a-zA-Z0-9._%+-]*@?[a-zA-Z0-9.-]*$".toRegex()
    
    return typingRegex.matches(email)
}