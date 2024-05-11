package com.example.homesecurity.ui.home

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.api.graphql.model.ModelSubscription
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

suspend fun getUserAlarmState(id: String): Boolean {
    return suspendCoroutine { continuation ->
        Amplify.API.query(
            ModelQuery.get(User::class.java, id),
            { response ->
                val user = response.data
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

suspend fun subscribeToUpdateUser() {
    return suspendCoroutine { _ ->
        Amplify.API.subscribe(
            ModelSubscription.onUpdate(User::class.java),
            { Log.i("ApiQuickStart Subscription", "Subscription established") },
            { response ->
                val data = response.data as User
                //fare robe
                Log.i("ApiQuickStart Subscription", "User modificato: ${data.alarm}")
            },
            { error ->
                Log.e("ApiQuickStart Subscription", "Errore durante la sottoscrizione", error)
            },
            { Log.i("ApiQuickStart Subscription", "Subscription completed") }
        )
    }
}