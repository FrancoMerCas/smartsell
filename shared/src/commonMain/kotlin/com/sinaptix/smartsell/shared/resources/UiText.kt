package com.sinaptix.smartsell.shared.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource

sealed class UiText {
    data class StringResource(
        val resId: org.jetbrains.compose.resources.StringResource
    ) : UiText()

    data class DynamicString(val value: String) : UiText()

    data class StringResourceWithArgs(
        val resId: org.jetbrains.compose.resources.StringResource,
        val args: Array<Any>
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is StringResourceWithArgs) return false
            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = resId.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId)
            is DynamicString -> value
            is StringResourceWithArgs -> stringResource(resId, *args)
        }
    }

    companion object {
        fun fromStringResource(resId: org.jetbrains.compose.resources.StringResource): UiText =
            StringResource(resId)

        fun fromString(value: String): UiText = DynamicString(value)

        fun fromStringResource(
            resId: org.jetbrains.compose.resources.StringResource,
            vararg args: Any
        ): UiText = StringResourceWithArgs(resId, arrayOf(*args))
    }
}