package com.alquranplusai.android.ui.components.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.alquranplusai.android.navigation.Screen

/**
 * Bottom navigation bar for main screens
 */
@Composable
fun HomeBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { navController.navigate(Screen.Home.route) }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Quran") },
            label = { Text("Quran") },
            selected = false,
            onClick = { navController.navigate(Screen.SurahList.route) }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Headphones, contentDescription = "Audio") },
            label = { Text("Audio") },
            selected = false,
            onClick = { navController.navigate(Screen.AudioPlayer.route) }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Bookmarks") },
            label = { Text("Bookmarks") },
            selected = false,
            onClick = { navController.navigate(Screen.Bookmarks.route) }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}
