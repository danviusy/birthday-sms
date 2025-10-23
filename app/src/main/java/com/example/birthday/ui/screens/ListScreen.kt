package com.example.birthday.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import com.example.birthday.ui.viewmodels.SmsViewModel
import java.time.LocalDate
import java.util.Calendar

@Composable
fun ListScreen(navController: NavHostController, viewModel: PersonViewModel, smsViewModel: SmsViewModel) {
    val persons by viewModel.persons.collectAsState()

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dob by remember {mutableStateOf<LocalDate?>(null)}

    val context = LocalContext.current


    val calendar = Calendar.getInstance()
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


            Button(onClick = { viewModel.addPerson(name, dob.toString(), phoneNumber) }) {
                Text("Legg til bursdag")
            }
            Button(onClick = { navController.navigate("start") }) {
                Text("Tilbake")
            }
            Button(onClick = { viewModel.deleteAll() }) {
                Text("Fjern alle")
            }

            Button(onClick = { smsViewModel.sendSms("90500335", "hei") }) {
                Text("SMS")
            }

            LazyColumn {
                items(persons) { person ->
                    Text(text = "Navn: ${person.name}, Fødselsdato: ${person.dob}, Telefon: ${person.phoneNumber}")
                    Button(onClick = { viewModel.delete(person.phoneNumber) }) {
                        Text("Slett")
                    }

                    Divider()
                }
            }

        }
    }
}