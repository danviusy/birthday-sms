package com.example.birthday.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleSmsWorker(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<SMSWorker>(20, TimeUnit.MINUTES) .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork("SMSWorker", ExistingPeriodicWorkPolicy.KEEP, workRequest)
}