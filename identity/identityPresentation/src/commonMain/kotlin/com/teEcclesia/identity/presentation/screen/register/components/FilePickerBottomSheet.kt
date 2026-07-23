package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_camera
import teecclesia.designsystem.generated.resources.ic_gallery
import teecclesia.designsystem.generated.resources.ic_folder
import teecclesia.designsystem.generated.resources.take_photo
import teecclesia.designsystem.generated.resources.from_gallery
import teecclesia.designsystem.generated.resources.from_files

enum class FilePickOption {
    CAMERA,
    GALLERY,
    FILES
}

@Composable
fun FilePickerBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onOptionSelected: (FilePickOption) -> Unit,
    target: UploadTarget?
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableNoRipple {
                        onOptionSelected(FilePickOption.CAMERA)
                        onDismiss()
                    }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_camera),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(Res.string.take_photo),
                    style = Theme.typography.bodyLarge,
                    color = Theme.colorScheme.onSurface
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableNoRipple {
                        onOptionSelected(FilePickOption.GALLERY)
                        onDismiss()
                    }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_gallery),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(Res.string.from_gallery),
                    style = Theme.typography.bodyLarge,
                    color = Theme.colorScheme.onSurface
                )
            }
            if (target != UploadTarget.PROFILE_PHOTO) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableNoRipple {
                            onOptionSelected(FilePickOption.FILES)
                            onDismiss()
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_folder),
                        contentDescription = null,
                        tint = Theme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(Res.string.from_files),
                        style = Theme.typography.bodyLarge,
                        color = Theme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
