package com.teEcclesia.identity.presentation.screen.attendance.register.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.presentation.screen.attendance.register.toDisplayString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.ic_qr_scan
import teecclesia.designsystem.generated.resources.search_users
import teecclesia.designsystem.generated.resources.search_users_hint

@Composable
fun MemberCodeInputField(
    code: String,
    onCodeChange: (String) -> Unit,
    onOpenScanner: () -> Unit,
    onManualSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    isScannerOpen: Boolean = false,
    focusRequester: FocusRequester? = null,
    suggestedUsers: List<AttendeeUserPreview> = emptyList(),
    isSuggestionsDropdownVisible: Boolean = false,
    isSearchingSuggestions: Boolean = false,
    onSelectSuggestedUser: (AttendeeUserPreview) -> Unit = {},
    onDismissSuggestions: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(Res.string.search_users).uppercase(),
                style = Theme.typography.labelSmall,
                color = Theme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
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

                    val isArabic = code.any { it in '\u0600'..'\u06FF' }

                    BasicTextField(
                        value = code,
                        onValueChange = onCodeChange,
                        modifier = fieldModifier,
                        textStyle = Theme.typography.titleLarge.copy(
                            color = Theme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = if (isArabic) 0.sp else 1.sp
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
                                        text = stringResource(Res.string.search_users_hint),
                                        style = Theme.typography.bodyMedium.copy(
                                            color = Theme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                        ),
                                        maxLines = 1
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

        DropdownMenu(
            expanded = isSuggestionsDropdownVisible && (suggestedUsers.isNotEmpty() || isSearchingSuggestions),
            onDismissRequest = onDismissSuggestions,
            properties = PopupProperties(focusable = false),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            if (isSearchingSuggestions && suggestedUsers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Theme.colorScheme.primary
                    )
                }
            } else {
                suggestedUsers.forEach { user ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (user.imageUrl.isNullOrBlank()) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                                        contentDescription = null,
                                        tint = Theme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Theme.colorScheme.secondaryContainer)
                                            .border(1.dp, Theme.colorScheme.outline, CircleShape)
                                            .padding(6.dp)
                                    )
                                } else {
                                    AsyncImage(
                                        model = user.imageUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Theme.colorScheme.outline)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = user.name,
                                        style = Theme.typography.titleMedium,
                                        color = Theme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        val userCode = user.code
                                        if (!userCode.isNullOrBlank()) {
                                            Text(
                                                text = userCode,
                                                style = Theme.typography.labelSmall,
                                                color = Theme.colorScheme.primary,
                                                fontWeight = FontWeight.Medium
                                            )

                                            Text(
                                                text = "•",
                                                style = Theme.typography.labelSmall,
                                                color = Theme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Text(
                                            text = stringResource(user.role.toDisplayString()),
                                            style = Theme.typography.labelSmall,
                                            color = Theme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        },
                        onClick = { onSelectSuggestedUser(user) }
                    )
                }
            }
        }
    }
}
