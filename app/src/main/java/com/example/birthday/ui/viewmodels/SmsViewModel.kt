package com.example.birthday.ui.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.telephony.SmsManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope

class SmsViewModel (application: Application) : AndroidViewModel(application) {

    fun setAllowSms(boolean: Boolean) {
        val sharedPreferences: SharedPreferences = application.getSharedPreferences(
            "My Preferences",
            android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("allowSms", boolean)
        editor.apply()

    }

    fun getAllowSms(): Boolean? {
        val sharedPreferences: SharedPreferences = application.getSharedPreferences(
            "My Preferences",
            android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("allowSms", false)
    }
    fun sendSms(phoneNumber: String, message: String) {

        if (!getAllowSms()!!) {
            Log.d("SMS", "Ikke tillatt å sende SMS")
            return
        }
        val smsManager = SmsManager.getDefault()
        Log.d("SMS", "Sender SMS")
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}