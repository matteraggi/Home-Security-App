package com.example.homesecurity.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var button = mutableStateOf(false).value;
    var buttonText = mutableStateOf(false).value

    fun changeButtonState() {
        button = !button;
        buttonText = !buttonText
    }

}