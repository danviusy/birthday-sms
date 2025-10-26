package com.example.birthday.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.birthday.ui.screens.EditScreen
import com.example.birthday.ui.screens.ListScreen
import com.example.birthday.ui.screens.PreferencesScreen
import com.example.birthday.ui.screens.StartScreen
import com.example.birthday.ui.viewmodels.PersonViewModel
import com.example.birthday.ui.viewmodels.SmsViewModel

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: PersonViewModel, smsViewModel: SmsViewModel) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController = navController) }
        composable("list") { ListScreen(navController = navController, viewModel) }
        composable("edit") { EditScreen(navController = navController, viewModel = viewModel) }
        composable("preferences") { PreferencesScreen(navController = navController, smsViewModel = smsViewModel) }
    }
}
