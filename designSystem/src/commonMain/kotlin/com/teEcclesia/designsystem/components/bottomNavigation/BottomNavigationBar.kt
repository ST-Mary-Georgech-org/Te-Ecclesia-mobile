package com.teEcclesia.designsystem.components.bottomNavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_home
import teecclesia.designsystem.generated.resources.ic_home_selected

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItemIndex: Int = 0,
    content: @Composable BottomNavigationScope.() -> Unit = {},
) {
    val scope = remember { BottomNavigationScopeImpl() }.apply {
        items.clear()
        content()
    }

    BottomNavigationBarContent(
        items = scope.items,
        selectedItemIndex = selectedItemIndex,
        onItemClick = { item ->
            val index = scope.items.indexOf(item)
            scope.items[index].entry.invoke()
        },
        modifier = modifier.background(Theme.colorScheme.background.secondary)
    )
}

@Preview
@Composable
private fun PreviewBottomNavigationBar() {
    TeEcclesiaTheme {
        Box(Modifier.fillMaxSize()) {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            ) {
                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = "Home",
                    entry = { }
                )

                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = "Categories",
                    entry = { }
                )
            }
        }
    }
}
