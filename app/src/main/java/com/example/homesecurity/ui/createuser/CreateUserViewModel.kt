package com.example.homesecurity.ui.createuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Person
import com.amplifyframework.datastore.generated.model.User
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateUserViewModel : ViewModel() {
    var userName: String = ""
    var physicalPin: String = ""

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
                        } else {
                            Log.i("CreatePerson", "Created Person with id: ${response.data.id}")
                            // Aggiorna il "User" con il nuovo thingsId
                            addThingId(user, physicalPin)
                        }
                    },
                    { error -> Log.e("CreatePerson", "Create failed", error) }
                )
            } catch (error: Exception) {
                Log.e("CreatePerson", "Failed to create person", error)
            }
        }
    }

    private fun addThingId(user: User, thingId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!user.thingsIds.contains(thingId)) {
                    user.thingsIds.add(thingId)
                    Amplify.API.mutate(
                        ModelMutation.update(user),
                        {
                            Log.i("checkAndUpdateDeviceId", "User updated")
                            _updateSuccess.value = true
                        },
                        { error ->
                            Log.e("checkAndUpdateDeviceId", "Failed to update user: $error")
                            _updateSuccess.value = false
                        }
                    )
                }
            } catch (error: Exception) {
                Log.e("checkAndUpdateDeviceId", "Failed to get current user", error)
            }
        }
    }
}
