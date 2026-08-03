package com.teEcclesia.notifications.domain.model

enum class NotificationType {
    ALERT,
    SYSTEM,
    REVIEW;
    
    companion object {
        fun fromStringOrDefault(type: String?): NotificationType {
            return entries.firstOrNull { it.name.equals(type, ignoreCase = true) } ?: SYSTEM
        }
    }
}
