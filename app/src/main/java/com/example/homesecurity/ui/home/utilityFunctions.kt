package com.example.homesecurity.ui.home

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun getCurrentUserId(): String {
    return suspendCoroutine { continuation ->
        val authClient = Amplify.Auth
        authClient.getCurrentUser(
            { user ->
                val userId = user.userId
                continuation.resume(userId)
            },
            { error ->
                Log.e("MyApp", "Error getting current user: $error")
                continuation.resume("") // Restituisci una stringa vuota in caso di errore
            }
        )
    }
}


suspend fun getUserEmail(id: String): String {
    return suspendCoroutine { continuation ->
        Amplify.API.query(
            ModelQuery.get(User::class.java, id),
            { response ->
                val user = response.data
                if (user != null) {
                    val userEmail = user.email
                    continuation.resume(userEmail)
                    Log.i("Amplify", "Retrieved user: $user")
                } else {
                    Log.w("Amplify", "User with ID $id not found")
                    continuation.resume("")
                }
            },
            { error ->
                Log.e("Amplify", "Error querying user: $error")
                continuation.resume("")
            }
        )
    }
}


suspend fun getUserAlarmState(id: String): Boolean {
    return suspendCoroutine { continuation ->
        Amplify.API.query(
            ModelQuery.get(User::class.java, id),
            { response ->
                val user = response.data
                Log.i("User", "response: $response")
                if (user != null) {
                    val alarmState = user.alarm
                    Log.i("User", "Retrieved user: $user")
                    continuation.resume(alarmState)
                } else {
                    Log.w("User", "User with ID $userId not found")
                    continuation.resume(false) // Possiamo restituire un valore predefinito in caso di errore
                }
            },
            { error ->
                Log.e("User", "Error querying user: $error")
                continuation.resume(false) // Restituiamo false in caso di errore
            }
        )
    }
}
