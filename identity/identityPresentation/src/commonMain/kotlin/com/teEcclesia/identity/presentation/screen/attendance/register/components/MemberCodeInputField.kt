package com.teEcclesia.identity.presentation.screen.attendance.register.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_qr_scan
import teecclesia.designsystem.generated.resources.user_code

@Composable
fun MemberCodeInputField(
    code: String,
    onCodeChange: (String) -> Unit,
    onOpenScanner: () -> Unit,
    onManualSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    isScannerOpen: Boolean = false,
    focusRequester: FocusRequester? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.user_code).uppercase(),
            style = Theme.typography.labelSmall,
            color = Theme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                val fieldModifier = Modifier
                    .fillMaxWidth()
                    .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                    .padding(bottom = 6.dp)

                BasicTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    modifier = fieldModifier,
                    textStyle = Theme.typography.headlineMedium.copy(
                        color = Theme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Theme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onManualSubmit() }
                    ),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (code.isEmpty()) {
                                Text(
                                    text = "20413",
                                    style = Theme.typography.headlineMedium.copy(
                                        color = Theme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 2.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.BottomCenter)
                        .background(Theme.colorScheme.outlineVariant)
                )
            }

            AnimatedVisibility(
                visible = code.isNotBlank(),
                enter = expandHorizontally(expandFrom = Alignment.End),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Theme.colorScheme.primary)
                        .clickableNoRipple { onManualSubmit() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = Theme.typography.titleLarge,
                        color = Theme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isScannerOpen) Theme.colorScheme.primaryContainer else Theme.colorScheme.surface
                    )
                    .border(
                        1.dp,
                        if (isScannerOpen) Theme.colorScheme.primary else Theme.colorScheme.outlineVariant,
                        RoundedCornerShape(12.dp)
                    )
                    .clickableNoRipple { onOpenScanner() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_qr_scan),
                    contentDescription = "Scan QR",
                    tint = if (isScannerOpen) Theme.colorScheme.primary else Theme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
