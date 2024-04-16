package com.example.homesecurity.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var button = mutableStateOf(false)
    var buttonText = mutableStateOf(false)



    fun changeButtonState() {
        button.value = !button.value
        buttonText.value = !buttonText.value
    }

}