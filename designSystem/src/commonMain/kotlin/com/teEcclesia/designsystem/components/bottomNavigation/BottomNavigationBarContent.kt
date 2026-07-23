package com.teEcclesia.designsystem.components.bottomNavigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.teEcclesia.designsystem.theme.theme.Theme
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_home
import teecclesia.designsystem.generated.resources.ic_home_selected
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigationBarContent(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemClick: (BottomNavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    BoxWithConstraints(modifier.height(74.dp)) {
        val itemWidth = maxWidth / items.size
        val indicatorWidth = itemWidth - 40.dp
        val indicatorOffset by animateDpAsState(
            targetValue = selectedItemIndex * itemWidth
        )

        Row(
            Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                BottomNavigationBarItem(
                    isSelected = index == selectedItemIndex,
                    selectedIcon = item.selectedIcon,
                    unselectedIcon = item.notSelectedIcon,
                    title = item.title,
                    onClick = { onItemClick(item) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Box(
            Modifier
                .padding(horizontal = 20.dp)
                .offset(x = indicatorOffset)
                .clip(
                    RoundedCornerShape(
                        bottomEnd = 4.dp,
                        bottomStart = 4.dp
                    )
                )
                .background(Theme.colorScheme.primary)
                .size(indicatorWidth, 4.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewBottomNavigationBar() {
    Theme {
        val items = listOf(
            BottomNavigationItem(
                selectedIcon = painterResource(Res.drawable.ic_home_selected),
                notSelectedIcon = painterResource(Res.drawable.ic_home),
                title = "Home",
                entry = {}
            ),
            BottomNavigationItem(
                selectedIcon = painterResource(Res.drawable.ic_home_selected),
                notSelectedIcon = painterResource(Res.drawable.ic_home),
                title = "Dukan",
                entry = {}
            ),
            BottomNavigationItem(
                selectedIcon = painterResource(Res.drawable.ic_home_selected),
                notSelectedIcon = painterResource(Res.drawable.ic_home),
                title = "Trends",
                entry = {}
            ),
            BottomNavigationItem(
                selectedIcon = painterResource(Res.drawable.ic_home_selected),
                notSelectedIcon = painterResource(Res.drawable.ic_home),
                title = "Profile",
                entry = {}
            )
        )
        var selectedItemIndex by remember {
            mutableIntStateOf(0)
        }

        BottomNavigationBarContent(
            items = items,
            selectedItemIndex = selectedItemIndex,
            onItemClick = {
                selectedItemIndex = items.indexOf(it)
            },
            modifier = Modifier.background(Color.White)
        )
    }
}
