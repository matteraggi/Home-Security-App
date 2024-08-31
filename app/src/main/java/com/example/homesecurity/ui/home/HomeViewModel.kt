package com.example.homesecurity.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Person
import com.amplifyframework.datastore.generated.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeViewModel : ViewModel() {
    private val _buttonState = MutableStateFlow(false)
    val buttonState = _buttonState.asStateFlow()

    fun changeButtonUi() {
        _buttonState.value = !_buttonState.value
        Log.i("buttonValue", "valore bottone: ${_buttonState.value}")
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


    fun fetchButtonStateFromDatabase(user: User) {
        viewModelScope.launch {
            try {
                if(user.alarm != _buttonState.value) {
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