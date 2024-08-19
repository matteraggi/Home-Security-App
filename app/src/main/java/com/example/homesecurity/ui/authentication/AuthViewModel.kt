package com.example.homesecurity.ui.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.User
import com.example.homesecurity.ui.home.getCurrentUserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthViewModel : ViewModel() {
    private val _pinValue = MutableStateFlow("")
    val pinValue = _pinValue.asStateFlow()

    init {
        // Fetch the pin when the ViewModel is created
        viewModelScope.launch {
            _pinValue.value = getPin()
        }
    }

    var showSetPinDialog by mutableStateOf(false)
        private set

    fun showSetPinDialog() {
        showSetPinDialog = true
    }

    fun dismissSetPinDialog() {
        showSetPinDialog = false
    }

    fun setPin(newPin: String) {
        _pinValue.value = newPin
    }

    private suspend fun getPin(): String {
        val id = getCurrentUserId()
        return suspendCoroutine { continuation ->
            Amplify.API.query(
                ModelQuery[User::class.java, id],
                { response ->
                    val user = response.data
                    if (user != null) {
                        Log.i("Pin", "Retrieved pin: ${user.pin}")
                        continuation.resume(user.pin)
                    } else {
                        continuation.resume("")
                    }
                },
                { error ->
                    Log.e("Pin", "Error querying user pin: $error")
                    continuation.resume("")
                }
            )
        }
    }
}
