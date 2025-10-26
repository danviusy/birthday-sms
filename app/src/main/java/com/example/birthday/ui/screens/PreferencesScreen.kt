package com.example.birthday.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.birthday.R
import com.example.birthday.ui.viewmodels.SmsViewModel

@Composable
fun PreferencesScreen(navController: NavHostController, smsViewModel: SmsViewModel) {

    var allowSms by remember { mutableStateOf(smsViewModel.getAllowSms() ?: false) }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                Image( // Logo
                    painter = painterResource(id = R.drawable.birthday_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Preferanser",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(16.dp))

                if (allowSms) {
                    OutlinedButton (
                        onClick = {
                            smsViewModel.setAllowSms(false)
                            allowSms = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp)
                    ) {
                        Text("Skru av SMS", fontSize = 20.sp)
                    }
                } else {
                    Button (
                        onClick = {
                            smsViewModel.setAllowSms(true)
                            allowSms = true
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp)
                    ) {
                        Text("Skru på SMS", fontSize = 20.sp)
                    }
                }



                Spacer(Modifier.height(24.dp))
                OutlinedButton(
                    onClick = { navController.navigate("start") },
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Text("Tilbake", fontSize = 20.sp)
                }
            }
        }
    }
}
