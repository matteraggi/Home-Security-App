package com.example.homesecurity.ui.createuser

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun CreateUserScreen(navController: NavController) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf(TextFieldValue()) }
    var physicalPin by remember { mutableStateOf(TextFieldValue()) }
    val createUserViewModel: CreateUserViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val updateSuccess by createUserViewModel.updateSuccess.collectAsState()

    // Gestione delle immagini
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                createUserViewModel.userImageUri = it
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap: Bitmap? ->
            bitmap?.let {
                // Converti il bitmap in un file temporaneo e ottieni l'URI
                val tempFile = File.createTempFile("image", ".jpg")
                FileOutputStream(tempFile).use { out ->
                    it.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                selectedImageUri = Uri.fromFile(tempFile)
                createUserViewModel.userImageUri = selectedImageUri
            }
        }
    )

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Utente creato con successo!")
            }
            navController.navigate(NotBottomBarPages.WifiList.route)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Crea il tuo Utente della casa", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                // RIGA PER L'IMMAGINE E IL CAMPO NOME
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 16.dp)
                        )
                    } else {
                        // Placeholder quando non c'è un'immagine selezionata
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_person_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Nome") },
                        modifier = Modifier
                            .weight(1f)  // Assicurati che occupi lo spazio rimanente nella riga
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // RIGA PER LE ICONE
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Imposta una foto profilo")

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_photo_library_24),
                            contentDescription = "Seleziona dalla galleria"
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { cameraLauncher.launch(null) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                            contentDescription = "Scatta una foto"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo per il Pin Fisico
                OutlinedTextField(
                    value = physicalPin,
                    onValueChange = { physicalPin = it },
                    label = { Text("Pin Fisico") },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    createUserViewModel.userName = userName.text
                    createUserViewModel.physicalPin = physicalPin.text
                    createUserViewModel.createPersonAndAddThingId(context)
                }) {
                    Text("Avanti")
                }
            }
        }
    )
}
