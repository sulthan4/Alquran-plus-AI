package com.alquranplusai.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alquranplusai.android.navigation.AlQuranNavHost
import com.alquranplusai.android.navigation.NavRoutes
import com.alquranplusai.android.ui.theme.AlQuranTheme
import com.alquranplusai.android.utils.PermissionManager
import android.Manifest
import android.os.Build

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlQuranTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                // Start at Splash Screen which handles checking first launch and permissions logic
                val startDestination = NavRoutes.SPLASH
                
                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar(currentRoute)) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, "Home") },
                                    label = { Text("Home") },
                                    selected = currentRoute == NavRoutes.HOME,
                                    onClick = {
                                        navController.navigate(NavRoutes.HOME) {
                                            popUpTo(NavRoutes.HOME) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Book, "Quran") },
                                    label = { Text("Quran") },
                                    selected = currentRoute == NavRoutes.SURAH_LIST,
                                    onClick = { 
                                        navController.navigate(NavRoutes.SURAH_LIST) {
                                            popUpTo(NavRoutes.HOME) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Headphones, "Audio") },
                                    label = { Text("Audio") },
                                    selected = currentRoute == NavRoutes.AUDIO || currentRoute == NavRoutes.AUDIO_PLAYER,
                                    onClick = { 
                                        navController.navigate(NavRoutes.AUDIO) {
                                            popUpTo(NavRoutes.HOME) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Bookmark, "Bookmarks") },
                                    label = { Text("Bookmarks") },
                                    selected = currentRoute == NavRoutes.BOOKMARKS,
                                    onClick = { 
                                        navController.navigate(NavRoutes.BOOKMARKS) {
                                            popUpTo(NavRoutes.HOME) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, "Profile") },
                                    label = { Text("Profile") },
                                    selected = currentRoute == NavRoutes.PROFILE,
                                    onClick = { 
                                        navController.navigate(NavRoutes.PROFILE) {
                                            popUpTo(NavRoutes.HOME) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    AlQuranNavHost(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                        startDestination = startDestination
                    )
                }
            }
        }
    }
    
    private fun shouldShowBottomBar(route: String?): Boolean {
        return route in listOf(
            NavRoutes.HOME,
            NavRoutes.SURAH_LIST,
            NavRoutes.BOOKMARKS,
            NavRoutes.SEARCH,
            NavRoutes.PROFILE,
            NavRoutes.AUDIO
        )
    }
}
