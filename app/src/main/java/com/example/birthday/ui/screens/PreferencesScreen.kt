package com.example.birthday.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.birthday.ui.viewmodels.SmsViewModel

@Composable
fun PreferencesScreen(navController: NavHostController, smsViewModel: SmsViewModel) {

    val smsViewModel: SmsViewModel = viewModel()

    var allowSms by remember { mutableStateOf(smsViewModel.getAllowSms() ?: false) }

    Scaffold { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            Button(onClick = { smsViewModel.setAllowSms(true) }) {
                Text("Skru på SMS")
            }

            Button(onClick = { smsViewModel.setAllowSms(false) }) {
                Text("Skru av SMS")
            }

            Button(onClick = { navController.navigate("start") }) {
                Text("Tilbake")
            }
        }
    }
}