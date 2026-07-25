package com.teEcclesia.identity.presentation.screen.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.chips.FilterChip
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.Gender
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.presentation.screen.requests.components.RegistrationRequestCard
import com.teEcclesia.identity.presentation.screen.requests.components.SortingOptionBottomSheet
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_menu
import teecclesia.designsystem.generated.resources.ic_search
import teecclesia.designsystem.generated.resources.registration_requests
import teecclesia.designsystem.generated.resources.search_requests

@Composable
fun RegistrationRequestsScreen(
    viewModel: RegistrationRequestsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    RegistrationRequestsContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun RegistrationRequestsContent(
    state: RegistrationRequestsUiState,
    listener: RegistrationRequestsInteractionListener
) {
    val listState = rememberLazyListState()
    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = listener::onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.background)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.registration_requests),
                style = Theme.typography.titleLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = state.searchQuery,
                shape = RoundedCornerShape(28.dp),
                onValueChange = listener::onSearchQueryChanged,
                labelText = stringResource(Res.string.search_requests),
                trailingIcon = painterResource(Res.drawable.ic_menu),
                leadingIcon = painterResource(Res.drawable.ic_search),
                onTrailingIconClick = { listener.onToggleSortingSheet(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(state.roles) { role ->
                    FilterChip(
                        selected = state.selectedRole == role,
                        onClick = { listener.onRoleFilterSelected(role) },
                        label = {
                            Text(
                                text = stringResource(role.toText()),
                                style = Theme.typography.bodyMedium,
                                color = if (state.selectedRole == role) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading && !state.isRefreshing) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Theme.colorScheme.primary)
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.requests, key = { it.id }) { request ->
                            RegistrationRequestCard(
                                onClick = {
                                    // Detail view click
                                },
                                imageUrl = request.imageUrl,
                                fullName = request.fullName,
                                requestDateTime = LocalDateTime(
                                    2024,
                                    6,
                                    1,
                                    12,
                                    0
                                ), // TODO: Replace with actual request date time
                                role = request.role,
                                stage = request.makhdoomProfile?.educationalStage,
                                year = request.makhdoomProfile?.educationalYear,
                                shamamsaStudyStatus = request.makhdoomProfile?.shamamsaStudyStatus,
                            )
                        }

                        if (state.isPagingLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }

                PaginationTrigger(
                    list = state.requests,
                    listState = listState,
                    remainingItemsToLoadNextPage = 5,
                    loadNextItems = listener::onLoadMore
                )
            }
        }

        SortingOptionBottomSheet(
            isVisible = state.isSortingSheetVisible,
            currentSortBy = state.sortBy,
            currentSortOrder = state.sortOrder,
            onSortOptionSelected = { sortBy, sortOrder ->
                listener.onSortByChanged(sortBy, sortOrder)
                listener.onToggleSortingSheet(false)
            },
            onDismiss = { listener.onToggleSortingSheet(false) }
        )
    }
}

@PreviewLightDark
@Composable
private fun RegistrationRequestsContentPreview() {
    var state by remember {
        mutableStateOf(
            RegistrationRequestsUiState(
                requests = listOf(
                    ProfileResponse(
                        id = "1",
                        firstName = "Joseph",
                        secondName = "Sameh",
                        thirdName = "Fouad",
                        lastName = "Nasr",
                        displayName = "Joseph Sameh",
                        fullName = "Joseph Sameh Fouad Nasr",
                        nationalId = "29901010101234",
                        phone = "01234567890",
                        homePhone = "0223456789",
                        email = "joseph@example.com",
                        isEmailVerified = true,
                        isPhoneVerified = true,
                        imageUrl = null,
                        job = "Software Engineer",
                        buildingNo = "10",
                        street = "Main Street",
                        streetBranch = "",
                        area = "Heliopolis",
                        floor = "3",
                        apartment = "12",
                        specialMark = "Near church",
                        gender = Gender.MALE,
                        status = UserStatus.PENDING_APPROVAL,
                        statusReason = null,
                        role = UserRole.KHADEM,
                        confessionPriest = null,
                        externalConfessionPriestName = "",
                        externalConfessionChurch = "",
                        externalConfessionPhone = "",
                        khademProfile = null,
                        kahenProfile = null,
                        parentProfile = null,
                        ordinationProfile = null,
                        makhdoomProfile = null
                    ),
                    ProfileResponse(
                        id = "2",
                        firstName = "Michael",
                        secondName = "George",
                        thirdName = "Fouad",
                        lastName = "Naguib",
                        displayName = "Michael George",
                        fullName = "Michael George Fouad Naguib",
                        nationalId = "30105050109876",
                        phone = "01098765432",
                        homePhone = "",
                        email = "michael@example.com",
                        isEmailVerified = true,
                        isPhoneVerified = true,
                        imageUrl = null,
                        job = "Student",
                        buildingNo = "5",
                        street = "El-Nasr St",
                        streetBranch = "",
                        area = "Maadi",
                        floor = "1",
                        apartment = "2",
                        specialMark = "",
                        gender = Gender.MALE,
                        status = UserStatus.PENDING_APPROVAL,
                        statusReason = null,
                        role = UserRole.MAKHDOOM,
                        confessionPriest = null,
                        externalConfessionPriestName = "",
                        externalConfessionChurch = "",
                        externalConfessionPhone = "",
                        khademProfile = null,
                        kahenProfile = null,
                        parentProfile = null,
                        ordinationProfile = null,
                        makhdoomProfile = null
                    )
                )
            )
        )
    }

    val listener = object : RegistrationRequestsInteractionListener {
        override fun onSearchQueryChanged(query: String) {
            state = state.copy(searchQuery = query)
        }

        override fun onRoleFilterSelected(role: UserRole?) {
            state = state.copy(selectedRole = role)
        }

        override fun onSortByChanged(sortBy: String, sortOrder: String) {
            state = state.copy(sortBy = sortBy, sortOrder = sortOrder)
        }

        override fun onToggleSortingSheet(isVisible: Boolean) {
            state = state.copy(isSortingSheetVisible = isVisible)
        }

        override fun onLoadMore() {}

        override fun onRefresh() {
            state = state.copy(isRefreshing = false)
        }

        override fun onApproveUser(userId: String) {}

        override fun onRejectUser(userId: String, reason: String) {}

        override fun onClickBack() {}
    }

    Preview(darkTheme = Theme.isDarkTheme) {
        RegistrationRequestsContent(
            state = state,
            listener = listener
        )
    }
}

