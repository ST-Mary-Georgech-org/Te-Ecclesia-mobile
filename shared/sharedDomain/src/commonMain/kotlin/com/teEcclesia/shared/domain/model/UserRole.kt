package com.teEcclesia.shared.domain.model

enum class UserRole {
    ADMIN,
    KHADEM,
    MAKHDOOM,
    PARENT,
    GUEST,
    KAHEN;

    companion object {
        fun fromStringOrDefault(value: String?): UserRole {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MAKHDOOM
        }
    }
}
