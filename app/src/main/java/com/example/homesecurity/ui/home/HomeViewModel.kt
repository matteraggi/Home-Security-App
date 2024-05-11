package com.example.homesecurity.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val _buttonState = MutableStateFlow(false)
    val buttonState = _buttonState.asStateFlow()

    fun changeButtonUi() {
        _buttonState.value = !_buttonState.value
        Log.i("buttonValue", "valore bottone: ${_buttonState.value}")
    }

    fun fetchButtonStateFromDatabase() {
        viewModelScope.launch {
            try {
                val alarmStatus = getUserAlarmState(getCurrentUserId())
                if(alarmStatus != _buttonState.value) {
                    withContext(Dispatchers.Main) { // Wrap changeButtonUi with Dispatchers.Main
                        changeButtonUi()
                    }
                }
            } catch (e: Exception) {
                Log.e("Amplify", "Errore durante il recupero dello stato del pulsante: $e")
            }
        }
    }

}