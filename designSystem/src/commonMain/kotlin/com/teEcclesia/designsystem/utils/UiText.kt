package com.teEcclesia.designsystem.utils

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.jvm.JvmName

sealed class UiText {
    data class DynamicString(val value: String = "") : UiText()
    data class StringRes(val resId: StringResource) : UiText()
    data class PluralRes(
        val resId: PluralStringResource,
        val quantity: Int,
        val formatArgs: List<Any> = emptyList()
    ) : UiText() {
        constructor(resId: PluralStringResource, quantity: Int, vararg args: Any) : this(
            resId,
            quantity,
            args.toList()
        )
    }
}

@Composable
fun UiText.asString(): String {
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.StringRes -> stringResource(resId)
        is UiText.PluralRes -> {
            if (formatArgs.isEmpty()) {
                pluralStringResource(resId, quantity)
            } else {
                pluralStringResource(resId, quantity, *formatArgs.toTypedArray())
            }
        }
    }
}

@Composable
@JvmName("asStringNullable")
fun UiText?.asString(): String? = this?.asString()

suspend fun UiText?.asStringSuspend(): String {
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.StringRes -> getString(resId)
        is UiText.PluralRes -> {
            if (formatArgs.isEmpty()) {
                getPluralString(resId, quantity)
            } else {
                getPluralString(resId, quantity, *formatArgs.toTypedArray())
            }
        }
        null -> ""
    }
}

fun StringResource.toUiText(): UiText.StringRes { return UiText.StringRes(this) }
