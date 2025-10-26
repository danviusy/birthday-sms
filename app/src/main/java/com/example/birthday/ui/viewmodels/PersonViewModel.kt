package com.example.birthday.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.birthday.data.Person
import com.example.birthday.repositories.PersonRepository
import com.example.birthday.ui.uistate.PersonUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class PersonViewModel (private val repository: PersonRepository, application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PersonUiState()) // State
    val uiState: StateFlow<PersonUiState> = _uiState

    fun setPerson(name: String, dob: String, phoneNumber: String) { // Legger detaljer om en person i State-verdiene
        _uiState.value = PersonUiState(
            name = name,
            dob = dob,
            phoneNumber = phoneNumber
        )
    }
    val persons: StateFlow<List<Person>> = repository.allPersons.map { list -> // Henter en liste med personer fra databasen og sorterer den basert på dato
        list.sortedBy { it.dob }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val birthdayToday: StateFlow<List<Person>> = persons.map { list -> // Henter en liste over folk som har bursdag i dag
        val today = LocalDate.now()
        list.filter { person ->
            val dob = LocalDate.parse(person.dob)
            dob.month == today.month && dob.dayOfMonth == today.dayOfMonth
        }.distinctBy { it.id }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addPerson(name: String, dob: String, phoneNumber: String) { // Legger til en person i databasen
        viewModelScope.launch {
            repository.insert(Person(name = name, dob = dob, phoneNumber = phoneNumber))
        }
    }

    fun deleteAll() { // Sletter alle radene i person-tabellen
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun delete(phoneNumber: String) { // Sletter en person basert på telefonnummer
        viewModelScope.launch { repository.delete(phoneNumber = phoneNumber) }
        Log.d("PersonViewModel", "Deleting person: $phoneNumber")
    }

    fun update(name: String, dob: String, phoneNumber: String) { // Oppdaterer en person i databasen
        viewModelScope.launch {
            repository.update(name, dob, phoneNumber)
        }
    }

}