package com.example.myal_quran.navigasi

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myal_quran.screen.DetailSurahScreen
import com.example.myal_quran.screen.SurahListScreen
import com.example.myal_quran.screen.HomeScreen
import com.example.myal_quran.viewmodel.DetailViewModel
import com.example.myal_quran.viewmodel.SurahViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

@Composable
fun QuranNavGraph(
    navController: NavHostController,
    account: GoogleSignInAccount?,
    onLoginClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                account = account,
                onLoginClicked = onLoginClicked,
                onLogoutClicked = onLogoutClicked
            )
        }
        composable("surah_list") {
            val viewModel: SurahViewModel = viewModel()
            SurahListScreen(
                navController = navController,
                viewModel = viewModel,
                account = account,
                onLogoutClicked = {
                    onLogoutClicked()
                    navController.navigate("home") {
                        popUpTo("surah_list") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 1
            val viewModel: DetailViewModel = viewModel()
            DetailSurahScreen(surahId = id, viewModel = viewModel, navController = navController)
        }
    }
}