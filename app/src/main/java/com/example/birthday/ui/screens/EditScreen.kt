package com.example.birthday.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birthday.R
import com.example.birthday.ui.viewmodels.PersonViewModel
import java.time.LocalDate
import java.util.Calendar

@Composable
fun EditScreen(navController: NavHostController, viewModel: PersonViewModel) {
    val personUiState = viewModel.uiState.collectAsState() // Ui_state


    var name by remember { mutableStateOf(personUiState.value.name) } // Henter nåværende navn som skal redigeres fra state
    var phoneNumber by remember { mutableStateOf(personUiState.value.phoneNumber) } // Henter tlf
    var dob by remember { mutableStateOf(LocalDate.parse(personUiState.value.dob)) } // Henter fødselsdato

    var showErrorMessage by remember { mutableStateOf(false) } // Boolean-verdi som brukes til å vise feilmeldingen om feltene er tomme
    val fieldsEmpty = name.isBlank() || phoneNumber.isBlank() || dob == null // Sjekker om feltene er tomme

    val context = LocalContext.current

    val calendar = Calendar.getInstance().apply { // Setter opp dato-pickeren
        set(Calendar.YEAR, dob.year)
        set(Calendar.MONTH, dob.monthValue - 1)
        set(Calendar.DAY_OF_MONTH, dob.dayOfMonth)
    }

    val datePickerDialog = DatePickerDialog( // DatePickerDialog
        context,
        { _, year, month, dayOfMonth ->
            dob = LocalDate.of(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image( // Logo
                painter = painterResource(id = R.drawable.birthday_icon),
                contentDescription = "Logo",
                modifier = Modifier.padding(8.dp)
            )

            Text("Rediger profil", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField( // Navn-felt
                value = name,
                onValueChange = { name = it },
                label = { Text("Navn") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField( // Tlf-felt
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Telefon") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            OutlinedTextField( // Dato-felt
                value = dob.toString(),
                onValueChange = {},
                label = { Text("Fødselsdato") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Velg dato"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Button(
                onClick = { // Knapp som oppdaterer data i databasen
                    if (fieldsEmpty) {
                        showErrorMessage = true
                    } else {
                        viewModel.update(name, dob.toString(), phoneNumber)
                        navController.navigate("list") // Navigerer tilbake til liste-skjermen
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text("Oppdater profil", fontSize = 20.sp)
            }


            if (showErrorMessage) { // Feilmelding hvis ikke alle felt er fylt, forhindrer lagring av null-verdier
                Text(
                    "Alle feltene må fylles ut",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            OutlinedButton(
                onClick = { navController.navigate("list") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text("Tilbake", fontSize = 18.sp)
            }
        }
    }
}
