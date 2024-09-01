package com.example.homesecurity.ui.createnewuser

import android.content.ContentResolver
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
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreateNewUserViewModel : ViewModel() {
    var userName: String = ""
    var userImageUri: Uri? = null

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> get() = _updateSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun createPerson(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val user = getUser(getCurrentUserId())

                val file = userImageUri?.let { getFileFromUri(context, it) }

                if (file != null) {
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
                            }
                            _isLoading.value = false
                        },
                        { error ->
                            Log.e("CreatePerson", "Create failed", error)
                            _isLoading.value = false
                        }
                    )

                } else {
                    val person = Person.builder()
                        .inside(true)
                        .name(userName)
                        .user(user)
                        .photo("")
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
                            }
                            _isLoading.value = false
                        },
                        { error ->
                            Log.e("CreatePerson", "Create failed", error)
                            _isLoading.value = false
                        }
                    )
                }

            } catch (error: Exception) {
                Log.e("CreatePerson", "Failed to create person", error)
                _isLoading.value = false
            }
        }
    }

    private fun encodeImageToBase64(file: File): String {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    // Ottenere un ContentResolver
    val contentResolver: ContentResolver = context.contentResolver

    // Creare un file temporaneo nella cache del contesto
    val tempFile = File.createTempFile("temp", null, context.cacheDir)

    try {
        // Aprire l'InputStream dall'URI
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            // Creare un FileOutputStream per scrivere nel file temporaneo
            val outputStream = FileOutputStream(tempFile)

            // Copiare i dati dall'InputStream al FileOutputStream
            inputStream.copyTo(outputStream)

            // Chiudere i flussi
            inputStream.close()
            outputStream.close()

            return tempFile
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}