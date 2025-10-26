package com.example.birthday.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birthday.components.Dialog
import com.example.birthday.ui.viewmodels.PersonViewModel
import java.time.LocalDate
import java.util.Calendar

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: PersonViewModel
) {
    val persons by viewModel.persons.collectAsState() // Liste av alle "venner" som er lagt til
    val birthdayToday by viewModel.birthdayToday.collectAsState() // Liste av alle som har bursdag i dag

    var name by remember { mutableStateOf("") } // Navn-verdi
    var phoneNumber by remember { mutableStateOf("") } // Tlf-verdi
    var dob by remember { mutableStateOf<LocalDate?>(null) } // Fødselsdato-verdi

    var showDialog by remember { mutableStateOf(false) } // Dialog som bruker under sletting av alle bursdager

    var showErrorMessage by remember { mutableStateOf(false) } // Boolean-verdi som brukes til å vise feilmeldingen om feltene er tomme
    val fieldsEmpty = name.isBlank() || phoneNumber.isBlank() || dob == null // Sjekker om feltene er tomme

    val context = LocalContext.current // Henter context for å bruke i DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog( // DatePickerDialog for å velge dato
        context,
        { _, year, month, dayOfMonth -> dob = LocalDate.of(year, month + 1, dayOfMonth) },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                OutlinedTextField( // Navn-felt
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Navn") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedTextField(
                    value = phoneNumber, // Telefonnr-felt
                    onValueChange = { phoneNumber = it },
                    label = { Text("Telefon") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedTextField( // Fødselsdato-felt vha. datepickerdialog
                    value = dob?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Fødselsdato") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Velg dato")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                if (showErrorMessage) { // Feilmelding hvis ikke alle felt er fylt, forhindrer lagring av null-verdier
                    Text(
                        "Alle feltene må fylles ut",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button( // Knapp som legger til bursdag i databasen
                    onClick = {
                        if (fieldsEmpty) { // Hvis feltene er tomme, vis feilmelding
                            showErrorMessage = true
                        } else {
                            viewModel.addPerson(name, dob.toString(), phoneNumber) // Sender data til viewmodel
                            // Tømmer feltene
                            name = ""
                            phoneNumber = ""
                            dob = null
                            // Tilbakestiller feilmeldingen
                            showErrorMessage = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Legg til bursdag", fontSize = 20.sp)
                }
                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    OutlinedButton( // Navigerer tilbake til start-skjermen
                        onClick = { navController.navigate("start") },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Spacer(Modifier.width(4.dp))
                        Text("Tilbake", fontSize = 18.sp)
                    }

                    Button( // Fjerner alle knappene
                        onClick = { showDialog = true },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Spacer(Modifier.width(4.dp))
                        Text("Fjern alle", fontSize = 18.sp)
                    }
                }

                if (showDialog) { // Dialog som dobbelsjekker om bruker vil slette alle bursdager
                    Dialog(
                        onDismissRequest = { showDialog = false },
                        onConfirmation = {
                            viewModel.deleteAll()
                            showDialog = false
                                         },
                        dialogTitle = "Fjern alle",
                        dialogText = "Er du sikker på at du vil fjerne alle bursdager?"
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            if (birthdayToday.isNotEmpty()) { // Liste av alle bursdager i dag
                item {
                    Text("Bursdag i dag", style = MaterialTheme.typography.headlineSmall)
                }
                items(birthdayToday) { person ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFBB9CBD0)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                person.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Tlf: ${person.phoneNumber}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Dato: ${person.dob}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
            }

            // Liste av alle bursdager
            item { Text("Fremtidige bursdager", style = MaterialTheme.typography.headlineSmall) }
            items(persons) { person ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                person.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Tlf: ${person.phoneNumber}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Dato: ${person.dob}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }


                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            OutlinedButton( // Knapp for å redigere detaljer om en person
                                onClick = {
                                    viewModel.setPerson(person.name, person.dob, person.phoneNumber)
                                    navController.navigate("edit")
                                },
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Tilbake",
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            Button( // Knapp for å slette en person
                                onClick = { viewModel.delete(person.phoneNumber) },
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Slett",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

