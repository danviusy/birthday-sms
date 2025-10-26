package com.example.birthday.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleSmsWorker(context: Context) {
    // Service igangsettes én gang i døgnet, altså 24 timer
    val workRequest = PeriodicWorkRequestBuilder<SMSWorker>(24, TimeUnit.HOURS) .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork("SMSWorker", ExistingPeriodicWorkPolicy.KEEP, workRequest)
}