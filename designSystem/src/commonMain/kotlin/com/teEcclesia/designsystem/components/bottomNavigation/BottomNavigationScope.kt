package com.teEcclesia.designsystem.components.bottomNavigation

import androidx.compose.ui.graphics.painter.Painter

interface BottomNavigationScope {
    fun bottomNavigationItem(
        notSelectedIcon: Painter,
        selectedIcon: Painter,
        title: String,
        entry: () -> Unit,
    ) {
        error("The method is not implemented")
    }

}

data class BottomNavigationItem(
    val notSelectedIcon: Painter,
    val selectedIcon: Painter,
    val title: String,
    val entry: () -> Unit,
)

internal class BottomNavigationScopeImpl : BottomNavigationScope {
    val items = mutableListOf<BottomNavigationItem>()

    override fun bottomNavigationItem(
        notSelectedIcon: Painter,
        selectedIcon: Painter,
        title: String,
        entry: () -> Unit,
    ) {
        items.add(BottomNavigationItem(notSelectedIcon, selectedIcon, title, entry))
    }
}