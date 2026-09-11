package com.teEcclesia.identity.presentation.screen.profile

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.util.AppLanguage

data class ProfileScreenState(
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val canAddUser: Boolean = false,
    val canSearchUsers: Boolean = false,
    val userRole: UserRole? = null,
    val fullName: String = "",
    val displayName: String = "",
    val userCode: String = "",
    val imageUrl: String? = null,
    val whatsAppLink: String? = null,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isRefreshing: Boolean = false,
    val isNotificationPermissionGranted: Boolean = true,
    val currentAcademicYear: String = ""
)

