package com.example.birthday.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(person: Person)

    @Query("SELECT * FROM person_table")
    fun getAllPersons(): Flow<List<Person>>

    @Query("DELETE FROM person_table")
    suspend fun deleteAll()

    @Query("DELETE FROM person_table WHERE phoneNumber = :phoneNumber")
    suspend fun delete(phoneNumber: String)
}



