package com.example.birthday.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "person_table")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dob: String,
    val phoneNumber: String
)