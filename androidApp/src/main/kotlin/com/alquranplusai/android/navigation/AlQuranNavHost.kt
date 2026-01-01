package com.alquranplusai.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alquranplusai.android.ui.screens.home.HomeScreen
import com.alquranplusai.android.ui.screens.quran.SurahListScreen
import com.alquranplusai.android.ui.screens.quran.JuzListScreen
import com.alquranplusai.android.ui.screens.quran.JuzViewScreen
import com.alquranplusai.android.ui.screens.quran.ReadingScreen
import com.alquranplusai.android.ui.screens.audio.AudioPlayerScreen
import com.alquranplusai.android.ui.screens.PermissionsScreen
import com.alquranplusai.android.ui.screens.bookmarks.BookmarksScreen
import com.alquranplusai.android.ui.screens.quiz.QuizListScreen
import com.alquranplusai.android.ui.screens.quiz.QuizPlayScreen
import com.alquranplusai.android.ui.screens.quiz.QuizResultScreen

import com.alquranplusai.android.ui.screens.settings.SettingsScreen
import com.alquranplusai.android.ui.screens.profile.ProfileScreen
import com.alquranplusai.android.ui.screens.profile.EditProfileScreen

@Composable
fun AlQuranNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.HOME
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(NavRoutes.SPLASH) {
            com.alquranplusai.android.ui.screens.SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(NavRoutes.ONBOARDING) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    // Check permissions? For now go to Home, or Permissions flow
                    // Given previous logic, let's route to Permissions if needed, or Home.
                    // For safety, let's route to Home. If permissions are needed, features will request them.
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.ONBOARDING) {
            com.alquranplusai.android.ui.screens.OnboardingScreen(
                onComplete = {
                    val permissions = arrayOf(
                        android.Manifest.permission.RECORD_AUDIO
                    ) + if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else emptyArray()

                    val permissionManager = com.alquranplusai.android.utils.PermissionManager(navController.context as android.app.Activity)
                    if (permissionManager.hasPermissions(permissions)) {
                         navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    } else {
                        navController.navigate(NavRoutes.PERMISSIONS) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(NavRoutes.HOME) {
            HomeScreen(
                navController = navController,
                onNavigateToReading = { surah, ayah ->
                    navController.navigate(NavRoutes.reading(surah, ayah))
                },
                onNavigateToSurahList = {
                    navController.navigate(NavRoutes.SURAH_LIST)
                },
                onNavigateToAudio = {
                    navController.navigate(NavRoutes.AUDIO)
                },
                onNavigateToSearch = {
                    navController.navigate(NavRoutes.SEARCH)
                },
                onNavigateToBookmarks = {
                    navController.navigate(NavRoutes.BOOKMARKS)
                },
                onNavigateToDailyQuiz = {
                    navController.navigate(NavRoutes.DAILY_CHALLENGE)
                }
            )
        }

        composable(NavRoutes.SURAH_LIST) {
            SurahListScreen(
                onSurahClick = { surahNumber ->
                    navController.navigate(NavRoutes.reading(surahNumber, 1))
                },
                onAudioClick = {
                    navController.navigate(NavRoutes.AUDIO_PLAYER)
                },
                onSearchClick = {
                    navController.navigate(NavRoutes.SEARCH)
                },
                onJuzClick = { juzNumber ->
                    navController.navigate("juz_view/$juzNumber")
                }
            )
        }

        composable(NavRoutes.JUZ_LIST) {
            JuzListScreen(
                onJuzClick = { juzNumber ->
                    navController.navigate("juz_view/$juzNumber")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.JUZ_VIEW, arguments = listOf(
            androidx.navigation.navArgument("juzNumber") { type = androidx.navigation.NavType.IntType }
        )) { backStackEntry ->
            val juzNumber = backStackEntry.arguments?.getInt("juzNumber") ?: 1
            JuzViewScreen(
                juzNumber = juzNumber,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SEARCH) {
            com.alquranplusai.android.ui.screens.search.SearchScreen(
                onNavigateToReading = { surah, ayah ->
                    navController.navigate(NavRoutes.reading(surah, ayah))
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVoiceSearch = { navController.navigate(NavRoutes.VOICE_SEARCH) }
            )
        }

        composable(NavRoutes.VOICE_SEARCH) {
            com.alquranplusai.android.ui.screens.search.VoiceSearchScreen(
                navController = navController
            )
        }
        
        composable(NavRoutes.BOOKMARKS) {
            BookmarksScreen(
                onNavigateToReading = { surah, ayah ->
                    navController.navigate(NavRoutes.reading(surah, ayah))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) },
                onNavigateToEditProfile = { navController.navigate(NavRoutes.EDIT_PROFILE) }
            )
        }
        
        composable(NavRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                onProfileUpdated = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateTo = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.READING_PREFERENCES) {
            com.alquranplusai.android.ui.screens.settings.ReadingPreferencesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.AUDIO_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.AudioSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.NOTIFICATION_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.NotificationSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.LANGUAGE_SETTINGS) {
             // LanguageSettingsScreen(onNavigateBack = { navController.popBackStack() })
             // Using placeholder if file not ready, but checked file list and it exists.
             com.alquranplusai.android.ui.screens.settings.LanguageSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
             )
        }

        composable(NavRoutes.ABOUT) {
             com.alquranplusai.android.ui.screens.settings.AboutScreen(
                navController = navController
             )
        }

        composable(NavRoutes.DISPLAY_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.DisplaySettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.PRIVACY_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.PrivacySettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.SECURITY_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.SecuritySettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.BACKUP_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.BackupSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.DOWNLOAD_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.DownloadSettingsScreen(
                navController = navController
            )
        }
        
        composable(NavRoutes.ACCOUNT_SETTINGS) {
            com.alquranplusai.android.ui.screens.settings.AccountSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.QUIZ_LIST) {
            QuizListScreen(
                onQuizClick = { quizId -> 
                    // Use string route manually or update NavRoutes
                     navController.navigate("quiz_play/$quizId")
                }
            )
        }

        composable(NavRoutes.QUIZ_PLAY, arguments = listOf(
            androidx.navigation.navArgument("quizId") { type = androidx.navigation.NavType.StringType }
        )) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizPlayScreen(
                quizId = quizId,
                onNavigateBack = { navController.popBackStack() },
                onQuizComplete = { resultId ->
                    navController.navigate("quiz_result/$resultId") {
                        popUpTo(NavRoutes.QUIZ_PLAY) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.QUIZ_RESULT, arguments = listOf(
            androidx.navigation.navArgument("quizId") { type = androidx.navigation.NavType.StringType }
        )) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizResultScreen(
                quizId = resultId,
                onNavigateHome = { navController.navigate(NavRoutes.HOME) { popUpTo(NavRoutes.HOME) { inclusive = true } } },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.READING, arguments = listOf(
            androidx.navigation.navArgument(NavArguments.SURAH_NUMBER) { type = androidx.navigation.NavType.IntType },
            androidx.navigation.navArgument(NavArguments.AYAH_NUMBER) { type = androidx.navigation.NavType.IntType }
        )) { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getInt(NavArguments.SURAH_NUMBER) ?: 1
            val ayahNumber = backStackEntry.arguments?.getInt(NavArguments.AYAH_NUMBER) ?: 1
            
            ReadingScreen(
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTafsirSelection = { navController.navigate(NavRoutes.TAFSIR_SELECTION) },
                onNavigateToAudioPlayer = { navController.navigate(NavRoutes.AUDIO_PLAYER) }
            )
        }
        
        composable(NavRoutes.TAFSIR_SELECTION) {
             com.alquranplusai.android.ui.screens.tafsir.TafsirSelectionScreen(
                 navController = navController
             )
        }

        composable(NavRoutes.AUDIO_PLAYER) {
            AudioPlayerScreen()
        }
        
        composable(NavRoutes.AUDIO) {
            AudioPlayerScreen()
        }

        composable(NavRoutes.PERMISSIONS) {
            PermissionsScreen(
                onPermissionsGranted = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.PERMISSIONS) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.DAILY_CHALLENGE) {
            com.alquranplusai.android.ui.screens.quiz.DailyQuizScreen(
                onNavigateBack = { navController.popBackStack() },
                onStartQuiz = { quizId ->
                    navController.navigate("quiz_play/$quizId")
                }
            )
        }
    }
}
