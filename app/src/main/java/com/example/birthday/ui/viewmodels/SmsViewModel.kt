package com.example.birthday.ui.viewmodels

import android.app.Application
import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

class SmsViewModel (application: Application) : AndroidViewModel(application) {
    fun sendSms(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        Log.d("SMS", "Sender SMS")
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

    }
}