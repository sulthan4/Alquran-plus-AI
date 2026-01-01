package com.alquranplusai.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom navigation bar with main app sections
 */

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = NavRoutes.HOME
    ),
    BottomNavItem(
        label = "Quran",
        icon = Icons.Default.MenuBook,
        route = NavRoutes.SURAH_LIST
    ),
    BottomNavItem(
        label = "Audio",
        icon = Icons.Default.Headphones,
        route = NavRoutes.AUDIO
    ),
    BottomNavItem(
        label = "Bookmarks",
        icon = Icons.Default.Bookmark,
        route = NavRoutes.BOOKMARKS
    ),
    BottomNavItem(
        label = "Profile",
        icon = Icons.Default.Person,
        route = NavRoutes.PROFILE
    )
)

@Composable
fun BottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
