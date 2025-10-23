package com.example.birthday.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.birthday.ui.viewmodels.PersonViewModel
import java.time.LocalDate
import java.util.Calendar
import kotlin.toString

@Composable
fun EditScreen(navController: NavHostController, viewModel: PersonViewModel) {
    val personUiState = viewModel.uiState.collectAsState()


    var name by remember { mutableStateOf(personUiState.value.name) }
    var phoneNumber by remember { mutableStateOf(personUiState.value.phoneNumber) }
    var dob by remember {mutableStateOf<LocalDate>(LocalDate.parse(personUiState.value.dob))}

    val context = LocalContext.current


    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, dob.year)
        set(Calendar.MONTH, dob.monthValue - 1)
        set(Calendar.DAY_OF_MONTH, dob.dayOfMonth)
    }


    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dob = LocalDate.of(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    Scaffold { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            Button(onClick = { navController.navigate("list") }) {
                Text("Tilbake")
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Navn") }
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Telefon") }
            )
            OutlinedTextField(
                dob?.toString() ?: "",
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
                }
            )
            Button(onClick = {
                viewModel.update(name, dob.toString(), phoneNumber)
                navController.navigate("list")
            }) {
                Text("Oppdater profil")
            }
        }
    }
}