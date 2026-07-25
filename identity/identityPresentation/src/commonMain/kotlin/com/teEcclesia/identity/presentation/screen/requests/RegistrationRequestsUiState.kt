package com.teEcclesia.identity.presentation.screen.requests

import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import org.jetbrains.compose.resources.StringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.all_roles
import teecclesia.designsystem.generated.resources.no
import teecclesia.designsystem.generated.resources.role_admin
import teecclesia.designsystem.generated.resources.role_khadem
import teecclesia.designsystem.generated.resources.role_makhdoom
import teecclesia.designsystem.generated.resources.role_parent
import teecclesia.designsystem.generated.resources.role_priest
import teecclesia.designsystem.generated.resources.yes
import teecclesia.designsystem.generated.resources.history
import teecclesia.designsystem.generated.resources.history_long

data class RegistrationRequestsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val isPagingLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedRole: UserRole? = null,
    val sortBy: String = "createdAt",
    val sortOrder: String = "DESC",
    val requests: List<ProfileResponse> = emptyList(),
    val isLastPage: Boolean = false,
    val page: Int = 0,
    val isSortingSheetVisible: Boolean = false,
    val roles: List<UserRole?> = listOf(null, UserRole.KHADEM, UserRole.MAKHDOOM, UserRole.PARENT, UserRole.KAHEN)
)

fun UserRole?.toText(): StringResource {
    return when (this) {
        null -> Res.string.all_roles
        UserRole.KHADEM -> Res.string.role_khadem
        UserRole.MAKHDOOM -> Res.string.role_makhdoom
        UserRole.PARENT -> Res.string.role_parent
        UserRole.KAHEN -> Res.string.role_priest
        UserRole.ADMIN -> Res.string.role_admin
        UserRole.GUEST -> Res.string.role_makhdoom
    }
}

fun ShamamsaStudyStatus.toText(): StringResource {
    return when (this) {
        ShamamsaStudyStatus.YES -> Res.string.yes
        ShamamsaStudyStatus.NO -> Res.string.no
        ShamamsaStudyStatus.LONG_AGO -> Res.string.yes
    }
}

fun ShamamsaStudyStatus.toHistory(): StringResource {
    return when (this) {
        ShamamsaStudyStatus.YES -> Res.string.history
        ShamamsaStudyStatus.NO -> Res.string.no
        ShamamsaStudyStatus.LONG_AGO -> Res.string.history_long
    }
}
