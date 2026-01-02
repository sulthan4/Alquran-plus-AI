package com.alquranplusai.android

import androidx.compose.ui.res.stringResource
import com.alquranplusai.android.R

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

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import org.koin.android.ext.android.inject
import com.alquranplusai.data.preferences.PreferencesManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val preferencesManager: PreferencesManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observe language changes and apply to app
        lifecycleScope.launch {
            preferencesManager.language.collect { languageCode ->
                if (languageCode.isNotEmpty()) {
                    val appLocale = LocaleListCompat.create(Locale(languageCode))
                    // AppCompatDelegate handles optimization (won't restart if already set)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }
        }

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
                                    icon = { Icon(Icons.Default.Home, stringResource(R.string.nav_home)) },
                                    label = { Text(stringResource(R.string.nav_home)) },
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
                                    icon = { Icon(Icons.Default.Book, stringResource(R.string.nav_quran)) },
                                    label = { Text(stringResource(R.string.nav_quran)) },
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
                                    icon = { Icon(Icons.Default.Headphones, stringResource(R.string.nav_audio)) },
                                    label = { Text(stringResource(R.string.nav_audio)) },
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
                                    icon = { Icon(Icons.Default.Bookmark, stringResource(R.string.nav_bookmarks)) },
                                    label = { Text(stringResource(R.string.nav_bookmarks)) },
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
                                    icon = { Icon(Icons.Default.Person, stringResource(R.string.nav_more)) },
                                    label = { Text(stringResource(R.string.nav_more)) },
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
