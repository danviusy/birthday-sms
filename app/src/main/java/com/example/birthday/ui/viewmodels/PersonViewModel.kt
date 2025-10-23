package com.example.birthday.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.birthday.data.Person
import com.example.birthday.repositories.PersonRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class PersonViewModel (private val repository: PersonRepository, application: Application) : AndroidViewModel(application) {
    val persons: StateFlow<List<Person>> = repository.allPersons.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPerson(name: String, dob: String, phoneNumber: String) {
        viewModelScope.launch {
            repository.insert(Person(name = name, dob = dob, phoneNumber = phoneNumber))
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun delete(phoneNumber: String) {
        viewModelScope.launch { repository.delete(phoneNumber = phoneNumber) }
        Log.d("PersonViewModel", "Deleting person: $phoneNumber")
    }

}