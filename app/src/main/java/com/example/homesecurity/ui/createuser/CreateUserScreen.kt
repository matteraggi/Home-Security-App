package com.example.homesecurity.ui.createuser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun CreateUserScreen(navController: NavController) {
    var userName by remember { mutableStateOf(TextFieldValue()) }
    var physicalPin by remember { mutableStateOf(TextFieldValue()) }
    val createUserViewModel: CreateUserViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val updateSuccess by createUserViewModel.updateSuccess.collectAsState()

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Utente creato con successo!")
            }
            navController.navigate("wifilist")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Crea il tuo Utente della casa", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Nome") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = physicalPin,
                    onValueChange = { physicalPin = it },
                    label = { Text("Pin Fisico") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    createUserViewModel.userName = userName.text
                    createUserViewModel.physicalPin = physicalPin.text
                    createUserViewModel.createPersonAndAddThingId()
                }) {
                    Text("Avanti")
                }
            }
        }
    )
}