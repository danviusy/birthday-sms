package com.example.birthday.service

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.birthday.data.AppDatabase
import com.example.birthday.repositories.PersonRepository
import com.example.birthday.ui.viewmodels.SmsViewModel
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class SMSWorker (context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams ) {
    override suspend fun doWork(): Result {
        Log.d("SMSWorker", "Arbeidet startet")
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "person_db"
        ).build()

        val repository = PersonRepository(db.personDao())

        val smsViewModel = SmsViewModel(applicationContext as Application)

        val persons = repository.allPersons.first()
        val today = LocalDate.now()

        for (person in persons) {
            smsViewModel.sendSms(person.phoneNumber, "Gratulerer med dagen ${person.name}!")

            val dob = LocalDate.parse(person.dob)
            if (dob.month == today.month && dob.dayOfMonth == today.dayOfMonth) {
                smsViewModel.sendSms(person.phoneNumber, "Gratulerer med dagen ${person.name}!")
            }
        }
        return Result.success()

    }
}