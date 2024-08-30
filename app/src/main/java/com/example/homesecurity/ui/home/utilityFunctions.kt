package com.example.homesecurity.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.api.graphql.model.ModelSubscription
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Person
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

suspend fun getUser(id: String): User {
    return suspendCoroutine { continuation ->
        Amplify.API.query(
            ModelQuery[User::class.java, id],
            { response ->
                val user = response.data
                if (user != null) {
                    Log.i("User", "Retrieved user: $user")
                    continuation.resume(user)
                }
            },
            { error ->
                Log.e("User", "Error querying user: $error")
            }
        )
    }
}

suspend fun getHomePeople(id: String): List<Person> {
    return suspendCoroutine { continuation ->
        Amplify.API.query(
            ModelQuery.list(Person::class.java, Person.USER.eq(id)),
            { response ->
                val paginatedResult = response.data
                val people: List<Person> = paginatedResult.items.toList()
                Log.i("People", "People list!")
                continuation.resume(people)
            },
            { Log.e("People", "Query failure", it)
                emptyList<Person>()
            }
        )
    }
}

suspend fun subscribeToUpdateUser(viewModel: HomeViewModel) {
    val id = getCurrentUserId()
    try {
        Amplify.API.subscribe(
            ModelSubscription.onUpdate(User::class.java),
            { Log.i("Amplify Subscription", "Subscription established") },
            { response ->
                Log.i("Amplify Subscription", "Un messaggio è stato ricevuto")
                val data = response.data as User

                if (id == data.id) { // da cambiare con filtro lato server e non client
                    return@subscribe
                }

                if (data.alarm != viewModel.buttonState.value) {
                    viewModel.changeButtonUi()
                    Log.i("Amplify Subscription", "User modificato: ${data}")
                }
            },
            { error ->
                Log.e("Amplify Subscription", "Errore durante la sottoscrizione", error)
                return@subscribe
            },
            { Log.i("Amplify Subscription", "Subscription completed") }
        )
    }
    catch (e: Exception) {
        Log.e("Amplify Subscription", "Errore durante subscription: $e")
    }
}

suspend fun changeButtonState(viewModel: HomeViewModel) {
    val id = getCurrentUserId()
    if (id.isEmpty()) {
        Log.e("Amplify Alarm Mutation", "ID utente nullo o vuoto")
        return
    }

    val newAlarmValue = !viewModel.buttonState.value

    val user = getUser(id)

    val updatedUser = User.builder().alarm(newAlarmValue).nfc(user.nfc).email(user.email).deviceIds(user.deviceIds).updatedAt(user.updatedAt).thingsIds(user.thingsIds).pin(user.pin).id(id).build()

    try {
        Amplify.API.mutate(
            ModelMutation.update(updatedUser),
            { _ ->
                Log.i("Amplify Alarm Mutation", "Allarme aggiornato: $updatedUser")
                viewModel.changeButtonUi()
            },
            { error ->
                Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $error")
            }
        )
    } catch (e: Exception) {
        Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $e")
    }
}

suspend fun changeButtonStateTo(state: Boolean) {
    val id = getCurrentUserId()
    if (id.isEmpty()) {
        Log.e("Amplify Alarm Mutation", "ID utente nullo o vuoto")
        return
    }

    val user = getUser(id)

    val updatedUser = User.builder().alarm(state).nfc(user.nfc).email(user.email).deviceIds(user.deviceIds).updatedAt(user.updatedAt).thingsIds(user.thingsIds).pin(user.pin).id(id).build()

    try {
        Amplify.API.mutate(
            ModelMutation.update(updatedUser),
            { _ ->
                Log.i("Amplify Alarm Mutation", "Allarme aggiornato: $updatedUser")
            },
            { error ->
                Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $error")
            }
        )
    } catch (e: Exception) {
        Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $e")
    }
}

suspend fun registerNFC(nfc: String) {
    val id = getCurrentUserId()
    if (id.isEmpty()) {
        Log.e("Amplify Alarm Mutation", "ID utente nullo o vuoto")
        return
    }

    val user = getUser(id)

    val updatedUser = User.builder().alarm(user.alarm).nfc(nfc).email(user.email).deviceIds(user.deviceIds).updatedAt(user.updatedAt).thingsIds(user.thingsIds).pin(user.pin).id(id).build()

    try {
        Amplify.API.mutate(
            ModelMutation.update(updatedUser),
            { _ ->
                Log.i("Amplify Alarm Mutation", "Allarme aggiornato: $updatedUser")
            },
            { error ->
                Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $error")
            }
        )
    } catch (e: Exception) {
        Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $e")
    }
}

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

fun getSelectedPerson(context: Context): String? {
    val sharedPref = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    return sharedPref.getString("selected_person_id", null)
}

suspend fun getPerson(id: String): Person? {
    return suspendCoroutine { continuation ->
        Amplify.API.query(
            ModelQuery[Person::class.java, id],
            { response ->
                val person = response.data
                if (person != null) {
                    continuation.resume(person)
                } else {
                    continuation.resume(null)
                }
            },
            { error ->
                Log.e("Person", "Query failure", error)
                continuation.resume(null)
            }
        )
    }
}

suspend fun changePersonInside(context: Context, state: Boolean){
    val personId = getSelectedPerson(context) ?: return

    val person = getPerson(personId) ?: return

    val updatedPerson = Person.builder().inside(state).name(person.name).updatedAt(person.updatedAt).id(personId).user(person.user).createdAt(person.createdAt).photo(person.photo).build()

    try {
        Amplify.API.mutate(
            ModelMutation.update(updatedPerson),
            { _ ->
                Log.i("Amplify Alarm Mutation", "Allarme aggiornato: $updatedPerson")
            },
            { error ->
                Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $error")
            }
        )
    } catch (e: Exception) {
        Log.e("Amplify Alarm Mutation", "Errore durante l'aggiornamento dell'utente: $e")
    }
}
