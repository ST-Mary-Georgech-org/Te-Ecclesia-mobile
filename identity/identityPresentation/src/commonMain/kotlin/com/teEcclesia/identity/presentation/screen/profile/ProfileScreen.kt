package com.teEcclesia.identity.presentation.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.button.AppSegmentedControl
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.preview.PreviewThemes
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.identity.presentation.screen.login.getName
import com.teEcclesia.shared.domain.model.UserRole
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.rememberQrKitPainter
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_new_user
import teecclesia.designsystem.generated.resources.enable_notifications
import teecclesia.designsystem.generated.resources.ic_bell
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.logout
import teecclesia.designsystem.generated.resources.profile
import teecclesia.designsystem.generated.resources.search_users
import teecclesia.designsystem.generated.resources.select_language
import teecclesia.designsystem.generated.resources.select_theme
import teecclesia.designsystem.generated.resources.join_whatsapp_group
import teecclesia.designsystem.generated.resources.academic_year
import teecclesia.designsystem.generated.resources.edit

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.checkNotificationPermission()
            viewModel.loadAcademicYear()
        }
    }

    ProfileContent(
        state = state,
        onClickNotifications = viewModel::onClickNotifications,
        onLanguageSelected = viewModel::onLanguageSelected,
        onThemeSelected = viewModel::onThemeSelected,
        onClickEditProfile = viewModel::onClickEditProfile,
        onClickSearchUsers = viewModel::onClickSearchUsers,
        onClickAddUser = viewModel::onClickAddUser,
        onClickLogout = viewModel::onClickLogout,
        onRefresh = viewModel::onRefresh,
        onClickEnableNotifications = viewModel::openNotificationSettings,
        onClickEditAcademicYear = viewModel::onClickEditAcademicYear
    )
}

@Composable
private fun ProfileContent(
    state: ProfileScreenState,
    onClickNotifications: () -> Unit,
    onLanguageSelected: (language: AppLanguage) -> Unit,
    onThemeSelected: (theme: AppTheme) -> Unit,
    onClickEditProfile: () -> Unit,
    onClickSearchUsers: () -> Unit,
    onClickAddUser: () -> Unit,
    onClickLogout: () -> Unit,
    onRefresh: () -> Unit,
    onClickEnableNotifications: () -> Unit,
    onClickEditAcademicYear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.profile),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            IconButton(onClick = onClickNotifications) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bell),
                    contentDescription = "Notifications",
                    tint = Theme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(Theme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (!state.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = state.imageUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                    contentDescription = "Profile Picture Placeholder",
                    tint = Theme.colorScheme.onSecondaryContainer
                )
            }
        }

        if (state.displayName.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.displayName,
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onBackground
            )
        }

        if (state.fullName.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.fullName,
                style = Theme.typography.bodyLarge,
                color = Theme.colorScheme.onSurfaceVariant
            )
        }

        if (state.userCode.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = state.userCode,
                style = Theme.typography.labelLarge,
                color = Theme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        QrCodeSection(
            userCode = state.userCode,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.select_language),
            style = Theme.typography.titleMedium,
            color = Theme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        AppSegmentedControl(
            options = listOf(AppLanguage.ENGLISH, AppLanguage.ARABIC),
            selectedOption = state.currentLanguage,
            onOptionSelected = onLanguageSelected,
            getName = { this.getName() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.select_theme),
            style = Theme.typography.titleMedium,
            color = Theme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        AppSegmentedControl(
            options = listOf(AppTheme.LIGHT, AppTheme.DARK),
            selectedOption = if (Theme.isDarkTheme) AppTheme.DARK else AppTheme.LIGHT,
            onOptionSelected = onThemeSelected,
            getName = { this.getName() },
            modifier = Modifier.fillMaxWidth()
        )

        if (state.userRole == UserRole.ADMIN) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Theme.colorScheme.surfaceContainerHighest,
                border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.academic_year),
                            style = Theme.typography.labelMedium,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = state.currentAcademicYear.ifBlank { "—" },
                            style = Theme.typography.titleMedium,
                            color = Theme.colorScheme.onBackground
                        )
                    }

                    AppButton(
                        type = AppButtonType.Secondary,
                        onClick = onClickEditAcademicYear,
                        text = stringResource(Res.string.edit)
                    )
                }
            }
        }


//        Spacer(modifier = Modifier.height(24.dp))

//        AppButton( //TODO: keep it until make this feature
//            type = AppButtonType.Secondary,
//            onClick = onClickEditProfile,
//            modifier = Modifier.fillMaxWidth(),
//            text = stringResource(Res.string.edit_profile)
//        )

            if (!state.isNotificationPermissionGranted) {
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(
                    type = AppButtonType.Secondary,
                    onClick = onClickEnableNotifications,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.enable_notifications),
                    enableSecondaryBackgroundColor = Theme.colorScheme.error.copy(alpha = 0.12f)
                )
            }

            state.whatsAppLink?.let {
                Spacer(modifier = Modifier.height(12.dp))
                AppButton(
                    type = AppButtonType.Primary,
                    onClick = { uriHandler.openUri(it) },
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.join_whatsapp_group)
                )
            }

        if (state.canSearchUsers) {
            Spacer(modifier = Modifier.height(12.dp))
            AppButton(
                type = AppButtonType.Primary,
                onClick = onClickSearchUsers,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.search_users)
            )
        }

        if (state.canAddUser) {
            Spacer(modifier = Modifier.height(12.dp))
            AppButton(
                type = AppButtonType.Primary,
                onClick = onClickAddUser,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.add_new_user)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AppButton(
            type = AppButtonType.Primary,
            onClick = onClickLogout,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.logout),
            state = state.actionButtonState,
            enablePrimaryBackgroundColor = Theme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
    }
}

@Composable
private fun QrCodeSection(
    userCode: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Theme.colorScheme.surfaceContainerHighest,
        border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (userCode.isNotBlank()) {
                    val qrPainter = rememberQrKitPainter(data = userCode)
                    Image(
                        painter = qrPainter,
                        contentDescription = "User QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@PreviewThemes
@Composable
private fun ProfileContentPreview() = Theme {
    ProfileContent(
        state = ProfileScreenState(
            displayName = "مينـا يسـي",
            fullName = "مينا يسي كامل حنا",
            userCode = "USR-10294",
            canSearchUsers = true,
            canAddUser = true,
            userRole = UserRole.ADMIN,
            whatsAppLink = "https://chat.whatsapp.com/EXAMPLE"
        ),
        onClickNotifications = {},
        onLanguageSelected = {},
        onThemeSelected = {},
        onClickEditProfile = {},
        onClickSearchUsers = {},
        onClickAddUser = {},
        onClickLogout = {},
        onRefresh = {},
        onClickEnableNotifications = {},
        onClickEditAcademicYear = {}
    )
}

