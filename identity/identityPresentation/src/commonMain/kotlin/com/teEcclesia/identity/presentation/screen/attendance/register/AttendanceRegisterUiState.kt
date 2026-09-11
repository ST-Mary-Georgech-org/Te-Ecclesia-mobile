package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.shared.domain.model.UserRole
import org.jetbrains.compose.resources.StringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.role_admin
import teecclesia.designsystem.generated.resources.role_guest
import teecclesia.designsystem.generated.resources.role_khadem
import teecclesia.designsystem.generated.resources.role_makhdoom
import teecclesia.designsystem.generated.resources.role_parent
import teecclesia.designsystem.generated.resources.role_priest

data class AttendanceRegisterUiState(
    val eventId: Long = 0,
    val serviceName: String = "",
    val eventName: String = "",
    val isResponsible: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPagingLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val totalAttendees: Long = 0,
    val attendees: List<EventAttendee> = emptyList(),
    val isScannerOpen: Boolean = false,
    val userCodeInput: String = "",
    val searchedUser: AttendeeUserPreview? = null,
    val isSearchingUser: Boolean = false,
    val searchUserError: UiText? = null,
    val isRemoveConfirmSheetOpen: Boolean = false,
    val removingAttendee: EventAttendee? = null,
    val isActionLoading: Boolean = false,
    val suggestedUsers: List<AttendeeUserPreview> = emptyList(),
    val isSearchingSuggestions: Boolean = false,
    val isSuggestionsDropdownVisible: Boolean = false
)

fun UserRole.toDisplayString(): StringResource = when (this) {
    UserRole.MAKHDOOM -> Res.string.role_makhdoom
    UserRole.KHADEM -> Res.string.role_khadem
    UserRole.PARENT -> Res.string.role_parent
    UserRole.KAHEN -> Res.string.role_priest
    UserRole.ADMIN -> Res.string.role_admin
    UserRole.GUEST -> Res.string.role_guest
}
