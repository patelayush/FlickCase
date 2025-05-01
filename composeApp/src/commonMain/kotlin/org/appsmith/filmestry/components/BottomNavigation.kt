package org.appsmith.filmestry.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.ic_filter
import filmestry.composeapp.generated.resources.ic_home
import filmestry.composeapp.generated.resources.ic_home_filled
import filmestry.composeapp.generated.resources.ic_search
import org.appsmith.filmestry.Screen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBar(
    currentRoute: Screen? = null,
    onItemClick: (Screen) -> Unit = {}
) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                56.dp + AppBarDefaults.bottomAppBarWindowInsets.asPaddingValues()
                    .calculateBottomPadding()
            ),
        backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
        elevation = AppBarDefaults.BottomAppBarElevation,
        windowInsets = AppBarDefaults.bottomAppBarWindowInsets
    ) {
        NavItem.entries.forEach { entry ->
            BottomNavigationItem(
                selected = currentRoute == entry.screen,
                onClick = {
                    onItemClick(entry.screen)
                },
                icon = {
                    Image(
                        painter = if (currentRoute == Screen.Home && entry.screen == Screen.Home) painterResource(Res.drawable.ic_home_filled) else
                            painterResource(entry.icon),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                },
                label = {
                    Text(
                        text = entry.screen.screenName,
                        fontWeight = if (currentRoute == entry.screen) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                alwaysShowLabel = false
            )
        }
    }
}

enum class NavItem(
    val screen: Screen,
    val icon: DrawableResource,
) {
    HOME(
        screen = Screen.Home,
        icon = Res.drawable.ic_home,
    ),
    SEARCH(
        screen = Screen.Search,
        icon = Res.drawable.ic_search,
    ),
    GENRES(
        screen = Screen.Genres,
        icon = Res.drawable.ic_filter,
    ),
}