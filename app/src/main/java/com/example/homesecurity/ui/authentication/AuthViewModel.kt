package com.example.homesecurity.ui.authentication

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.User
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
class AuthViewModel : ViewModel() {
    private val _pinValue = MutableStateFlow("")
    val pinValue = _pinValue.asStateFlow()

    private val _savedNFC = MutableStateFlow(0)
    val savedNFC = _savedNFC.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _pinValue.value = getPin()
            _savedNFC.value = getNFC()
            _isLoading.value = false
        }
    }

    var showSetPinDialog by mutableStateOf(false)
        private set

    var showNFCDialog by mutableStateOf(false)
        private set

    fun showSetPinDialog() {
        showSetPinDialog = true
    }

    fun dismissSetPinDialog() {
        showSetPinDialog = false
    }

    fun showNFCDialog() {
        showNFCDialog = true
    }

    fun dismissNFCDialog() {
        showNFCDialog = false
    }

    suspend fun setPin(newPin: String) {
        val id = getCurrentUserId()

        if (id.isEmpty()) {
            Log.e("Amplify Alarm Mutation", "ID utente nullo o vuoto")
            return
        }

        val user = getUser(id)

        val updatedUser = User.builder().alarm(user.alarm).nfc(user.nfc).email(user.email).deviceIds(user.deviceIds).updatedAt(user.updatedAt).thingsIds(user.thingsIds).pin(newPin).id(id).build()

        try {
            Amplify.API.mutate(
                ModelMutation.update(updatedUser),
                { _ ->
                    Log.i("Amplify Alarm Mutation", "Allarme aggiornato: $updatedUser")
                    _pinValue.value = newPin
                },
                { error ->
                    Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $error")
                }
            )
        } catch (e: Exception) {
            Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $e")
        }
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

    private suspend fun getNFC(): Int {
        val id = getCurrentUserId()
        return suspendCoroutine { continuation ->
            Amplify.API.query(
                ModelQuery[User::class.java, id],
                { response ->
                    val user = response.data
                    if (user != null) {
                            continuation.resume(response.data.nfc.size)
                    } else {
                        continuation.resume(0)
                    }
                },
                { error ->
                    continuation.resume(0)
                }
            )
        }
    }
}
