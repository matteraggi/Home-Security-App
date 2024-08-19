package com.example.homesecurity.ui.createnewuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Person
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateNewUserViewModel : ViewModel() {
    var userName: String = ""

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> get() = _updateSuccess

    fun createPersonAndAddThingId() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUser(getCurrentUserId())

                val person = Person.builder().inside(true).name(userName).user(user).build()

                // Creazione della "Person"
                Amplify.API.mutate(
                    ModelMutation.create(person),
                    { response ->
                        if (response.hasErrors()) {
                            Log.e("CreatePerson", "Error: ${response.errors.first().message}")
                            _updateSuccess.value = false
                        } else {
                            Log.i("CreatePerson", "Created Person with id: ${response.data.id}")
                            _updateSuccess.value = true
                        }
                    },
                    { error -> Log.e("CreatePerson", "Create failed", error) }
                )
            } catch (error: Exception) {
                Log.e("CreatePerson", "Failed to create person", error)
            }
        }
    }
}
