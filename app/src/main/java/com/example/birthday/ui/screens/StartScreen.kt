package com.example.birthday.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birthday.R

@Composable
fun StartScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image( // Logo
                painter = painterResource(id = R.drawable.birthday_icon),
                contentDescription = "Logo",
                modifier = Modifier.padding(8.dp)
            )

            Text( // Tittel
                text = "Velkommen!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            Button( // Navigerer til main-skjermen, altså liste av bursdager
                onClick = { navController.navigate("list") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text("Bursdagsliste", fontSize = 20.sp)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton( // Navigerer til preferanse-skjermen
                onClick = { navController.navigate("preferences") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)

            ) {
                Text("Preferanser", fontSize = 20.sp)
            }
        }
    }
}
