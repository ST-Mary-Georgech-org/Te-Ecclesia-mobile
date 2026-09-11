package com.teEcclesia.identity.presentation.screen.academicYear

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.style.TextAlign
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.academic_year
import teecclesia.designsystem.generated.resources.academic_year_confirmation_message
import teecclesia.designsystem.generated.resources.academic_year_confirmation_title
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.edit_academic_year
import teecclesia.designsystem.generated.resources.enter_academic_year
import teecclesia.designsystem.generated.resources.failed_to_load_academic_year
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.retry
import teecclesia.designsystem.generated.resources.save_changes

@Composable
fun AcademicYearSettingsScreen(
    viewModel: AcademicYearSettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AcademicYearSettingsContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun AcademicYearSettingsContent(
    state: AcademicYearSettingsUiState,
    listener: AcademicYearSettingsInteractionListener,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = listener::onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = listener::onClickBack) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Theme.colorScheme.onBackground
                        )
                    }

                    Text(
                        text = stringResource(Res.string.edit_academic_year),
                        style = Theme.typography.headlineSmall,
                        color = Theme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Theme.colorScheme.primary)
                    }
                } else if (state.isLoadFailed) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.failed_to_load_academic_year),
                            style = Theme.typography.bodyLarge,
                            color = Theme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AppButton(
                            type = AppButtonType.Primary,
                            text = stringResource(Res.string.retry),
                            onClick = listener::onRetryLoad
                        )
                    }
                } else {
                    CustomTextField(
                        value = state.academicYear,
                        onValueChange = listener::onAcademicYearChanged,
                        labelText = stringResource(Res.string.academic_year),
                        supportingText = stringResource(Res.string.enter_academic_year),
                        errorText = state.academicYearError?.asString(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    AppButton(
                        type = AppButtonType.Primary,
                        onClick = listener::onClickSave,
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.save_changes),
                        state = state.saveButtonState
                    )
                }
            }
        }

        BottomSheet(
            isVisible = state.isConfirmDialogOpen,
            onDismiss = listener::onDismissConfirmDialog
        ) {
            Text(
                text = stringResource(Res.string.academic_year_confirmation_title),
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.academic_year_confirmation_message),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    type = AppButtonType.Secondary,
                    onClick = listener::onDismissConfirmDialog,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.cancel)
                )

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = listener::onConfirmSave,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.confirm)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AcademicYearSettingsScreenPreview() = Theme {
    Preview {
        AcademicYearSettingsContent(
            state = AcademicYearSettingsUiState(
                academicYear = "2026"
            ),
            listener = object : AcademicYearSettingsInteractionListener {
                override fun onAcademicYearChanged(year: String) {}
                override fun onClickSave() {}
                override fun onConfirmSave() {}
                override fun onDismissConfirmDialog() {}
                override fun onClickBack() {}
                override fun onRetryLoad() {}
                override fun onRefresh() {}
            }
        )
    }
}
