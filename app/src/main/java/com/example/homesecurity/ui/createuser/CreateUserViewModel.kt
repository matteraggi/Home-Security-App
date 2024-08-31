package com.example.homesecurity.ui.createuser

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Person
import com.amplifyframework.datastore.generated.model.User
import com.example.homesecurity.ui.createnewuser.getFileFromUri
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class CreateUserViewModel : ViewModel() {
    var userName: String = ""
    var physicalPin: String = ""
    var userImageUri: Uri? = null  // Nuova proprietà per l'immagine

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> get() = _updateSuccess

    fun createPersonAndAddThingId(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUser(getCurrentUserId())

                val file = userImageUri?.let { getFileFromUri(context, it) }

                if (file != null) {
                    // Converti l'immagine in Base64 usando il file temporaneo
                    val base64Image = encodeImageToBase64(file)

                    val person = Person.builder()
                        .inside(true)
                        .name(userName)
                        .user(user)
                        .photo(base64Image)
                        .build()

                    Amplify.API.mutate(
                        ModelMutation.create(person),
                        { response ->
                            if (response.hasErrors()) {
                                Log.e("CreatePerson", "Error: ${response.errors.first().message}")
                                _updateSuccess.value = false
                            } else {
                                Log.i("CreatePerson", "Created Person with id: ${response.data.id}")
                                _updateSuccess.value = true
                                addThingId(user, physicalPin)
                            }
                        },
                        { error -> Log.e("CreatePerson", "Create failed", error) }
                    )

                } else {
                    Log.e("CreatePerson", "Failed to create person")
                }

            } catch (error: Exception) {
                Log.e("CreatePerson", "Failed to create person", error)
            }
        }
    }

    private fun encodeImageToBase64(file: File): String {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
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
