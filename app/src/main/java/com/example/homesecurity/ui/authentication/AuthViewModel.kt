package com.example.homesecurity.ui.authentication

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

    private val _savedNFC = MutableStateFlow(false)
    val savedNFC = _savedNFC.asStateFlow()

    init {
        viewModelScope.launch {
            _pinValue.value = getPin()
            _savedNFC.value = getNFC()
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
                        continuation.resume(user.pin)
                    } else {
                        continuation.resume("")
                    }
                },
                { error ->
                    continuation.resume("")
                }
            )
        }
    }

    private suspend fun getNFC(): Boolean {
        val id = getCurrentUserId()
        return suspendCoroutine { continuation ->
            Amplify.API.query(
                ModelQuery[User::class.java, id],
                { response ->
                    val user = response.data
                    if (user != null) {
                        if(user.nfc.isNotEmpty()) {
                            continuation.resume(true)
                        }
                        else{
                            continuation.resume(false)
                        }
                    } else {
                        continuation.resume(false)
                    }
                },
                { error ->
                    continuation.resume(false)
                }
            )
        }
    }
}
