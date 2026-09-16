package com.teEcclesia.notifications.presentation.screen.adminSend

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.model.UserRole
import org.jetbrains.compose.resources.StringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.all_roles
import teecclesia.designsystem.generated.resources.role_admin
import teecclesia.designsystem.generated.resources.role_guest
import teecclesia.designsystem.generated.resources.role_khadem
import teecclesia.designsystem.generated.resources.role_makhdoom
import teecclesia.designsystem.generated.resources.role_parent
import teecclesia.designsystem.generated.resources.role_priest
import teecclesia.designsystem.generated.resources.specific_users
import teecclesia.designsystem.generated.resources.target_group

data class AdminSendNotificationUiState(
    val title: String = "",
    val titleError: UiText? = null,
    val body: String = "",
    val bodyError: UiText? = null,
    val targetMode: RecipientTargetMode = RecipientTargetMode.SPECIFIC_USERS,
    val userSearchQuery: String = "",
    val suggestedUsers: List<AttendeeUserPreview> = emptyList(),
    val isSearchingSuggestions: Boolean = false,
    val isSuggestionsDropdownVisible: Boolean = false,
    val selectedUsers: List<AttendeeUserPreview> = emptyList(),
    val selectedRole: UserRole? = null,
    val isRoleDropdownExpanded: Boolean = false,
    val educationalStages: List<LookupResponse> = emptyList(),
    val selectedEducationalStage: LookupResponse? = null,
    val isStageSheetVisible: Boolean = false,
    val isLoadingStages: Boolean = false,
    val isStageLoadFailed: Boolean = false,
    val payloadItems: List<PayloadItemUiState> = emptyList(),
    val isPayloadExpanded: Boolean = false,
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val isSuccessBannerVisible: Boolean = false
)

fun RecipientTargetMode.toDisplayTitle(): StringResource = when (this) {
    RecipientTargetMode.SPECIFIC_USERS -> Res.string.specific_users
    RecipientTargetMode.TARGET_GROUP -> Res.string.target_group
}

fun UserRole?.toRoleDisplayTitle(): StringResource = when (this) {
    null -> Res.string.all_roles
    UserRole.ADMIN -> Res.string.role_admin
    UserRole.KHADEM -> Res.string.role_khadem
    UserRole.MAKHDOOM -> Res.string.role_makhdoom
    UserRole.PARENT -> Res.string.role_parent
    UserRole.KAHEN -> Res.string.role_priest
    UserRole.GUEST -> Res.string.role_guest
}

val AdminSendNotificationUiState.isEducationalStageVisible: Boolean
    get() = selectedRole == null || selectedRole == UserRole.KHADEM || selectedRole == UserRole.MAKHDOOM

