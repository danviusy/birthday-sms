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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import com.example.birthday.ui.viewmodels.PersonViewModel
import com.example.birthday.ui.viewmodels.SmsViewModel
import java.time.LocalDate
import java.util.Calendar

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: PersonViewModel,
    smsViewModel: SmsViewModel
) {
    val persons by viewModel.persons.collectAsState()
    val birthdayToday by viewModel.birthdayToday.collectAsState()

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf<LocalDate?>(null) }
    var showErrorMessage by remember { mutableStateOf(false) }

    val fieldsEmpty = name.isBlank() || phoneNumber.isBlank() || dob == null
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
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
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Navn") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Telefon") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutlinedTextField(
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

                if (showErrorMessage) {
                    Text(
                        "Alle feltene må fylles ut",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (fieldsEmpty) showErrorMessage = true
                        else {
                            viewModel.addPerson(name, dob.toString(), phoneNumber)
                            name = ""
                            phoneNumber = ""
                            dob = null
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
                    OutlinedButton(
                        onClick = { navController.navigate("start") },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Spacer(Modifier.width(4.dp))
                        Text("Tilbake", fontSize = 18.sp)
                    }

                    Button(
                        onClick = { viewModel.deleteAll() },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Spacer(Modifier.width(4.dp))
                        Text("Fjern alle", fontSize = 18.sp)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            if (birthdayToday.isNotEmpty()) {
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
                            OutlinedButton(
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

                            Button(
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

