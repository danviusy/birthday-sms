package com.example.birthday.repositories

import com.example.birthday.data.Person
import com.example.birthday.data.PersonDao

class PersonRepository(private val dao: PersonDao) {
    val allPersons = dao.getAllPersons()

    suspend fun insert(person: Person) {
        dao.insert(person)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun delete(phoneNumber: String) {
        dao.delete(phoneNumber)
    }

}