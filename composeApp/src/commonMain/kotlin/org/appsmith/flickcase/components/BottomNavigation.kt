package org.appsmith.flickcase.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import flickcase.composeapp.generated.resources.Res
import flickcase.composeapp.generated.resources.ic_about
import flickcase.composeapp.generated.resources.ic_filter
import flickcase.composeapp.generated.resources.ic_home
import flickcase.composeapp.generated.resources.ic_search
import org.appsmith.flickcase.Screen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBar(
    currentRoute: Screen? = null,
    onItemClick: (Screen) -> Unit = {}
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .height(
                70.dp + AppBarDefaults.bottomAppBarWindowInsets.asPaddingValues()
                    .calculateBottomPadding()
            ),
        containerColor = Color.Transparent,
        tonalElevation = AppBarDefaults.BottomAppBarElevation,
        windowInsets = AppBarDefaults.bottomAppBarWindowInsets,
    ) {
        NavItem.entries.forEach { entry ->
            val isSelected = currentRoute == entry.screen
            val animatedAlpha: Float by animateFloatAsState(
                targetValue = if (isSelected) 360f else 0f,
                label = "rotate",
                animationSpec = tween(1000)
            )
            Column(
                modifier = Modifier
                    .animateContentSize(tween())
                    .weight(1f)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }){
                        onItemClick(entry.screen)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                ) {
                    Image(
                        painter = painterResource(entry.icon),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            color = if(isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.rotate(
                            if (isSelected) animatedAlpha else 0f
                        )
                    )
                }
                if (isSelected) {
                    Text(
                        text = entry.screen.screenName,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.2.sp,
                    )
                }
            }
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
    About(
        screen = Screen.About,
        icon = Res.drawable.ic_about,
    ),
}