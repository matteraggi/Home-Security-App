package com.example.homesecurity.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var button = mutableStateOf(false)
    var buttonText = mutableStateOf(false)

    fun changeButtonUi() {
        button.value = !button.value
        buttonText.value = !buttonText.value
    }

    fun changeButtonUiTo(value: Boolean){
        button.value = value
        buttonText.value = value
    }

    fun fetchButtonStateFromDatabase() {
        viewModelScope.launch {
            try {
                val alarmStatus = getUserAlarmState(getCurrentUserId())
                changeButtonUiTo(alarmStatus)
                Log.i("Amplify", "button state : $alarmStatus")
            } catch (e: Exception) {
                Log.e("Amplify", "Errore durante il recupero dello stato del pulsante: $e")
            }
        }
    }

}