package com.example.homesecurity.ui.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homesecurity.MainActivity
import com.example.homesecurity.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AuthScreen() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val pin by authViewModel.pinValue.collectAsState(initial = "")
    val isPinVisible = remember { mutableStateOf(false) }
    val nfc by authViewModel.savedNFC.collectAsState(initial = 0)
    val isLoading by authViewModel.isLoading.collectAsState()

    if (authViewModel.showSetPinDialog) {
        SetPinDialog(viewModel = authViewModel, onDismiss = { authViewModel.dismissSetPinDialog() })
    }

    if (authViewModel.showNFCDialog) {
        SetNFCDialog(onDismiss = {
            authViewModel.dismissNFCDialog()
            (context as MainActivity).disableForegroundDispatch()
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Contenuto normale da mostrare dopo il caricamento
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardBoxWithIconAndNumber(iconResId = R.drawable.baseline_fingerprint_24, number = "0")
                if (nfc > 0) {
                    CardBoxNFC(iconResId = R.drawable.baseline_nfc_24, colorResource(R.color.dark_green), nfc)
                } else {
                    CardBoxNFC(iconResId = R.drawable.baseline_nfc_24, Color.Black, 0)
                }
            }

            Card(
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 16.dp)
                    .clickable {
                        if (pin.isNotEmpty()) {
                            isPinVisible.value =
                                !isPinVisible.value  // Inverti la visibilità del PIN al clic
                        } else {
                            authViewModel.showSetPinDialog()
                        }
                    },
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Pin", color = Color.Black, fontSize = 16.sp)
                    Text(
                        text = if (pin.isEmpty()) {
                            "Imposta il PIN"
                        } else {
                            if (isPinVisible.value) {
                                pin  // Mostra il PIN effettivo
                            } else {
                                "*".repeat(pin.length)  // Mostra una stringa di asterischi della stessa lunghezza del PIN
                            }
                        },
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Registra nuovo metodo di autenticazione:",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.width(300.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardBoxWithIcon(iconResId = R.drawable.baseline_fingerprint_24) {}
                CardBoxWithIcon(iconResId = R.drawable.baseline_nfc_24) {
                    authViewModel.showNFCDialog()
                    (context as MainActivity).enableForegroundDispatch()
                }
                CardBoxWithText(text = "Cambia Pin") {
                    authViewModel.showSetPinDialog()
                }
            }
        }
    }
}


@Composable
fun CardBoxWithIconAndNumber(iconResId: Int, number: String) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(140.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(60.dp)
            )
            Text(text = number, color = Color.Black, fontSize = 20.sp)
        }
    }
}

@Composable
fun CardBoxNFC(iconResId: Int, color: Color, number: Number) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(140.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(60.dp)
            )
            Text(text = "$number", color = Color.Black, fontSize = 20.sp)
        }
    }
}

@Composable
fun CardBoxWithIcon(iconResId: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clickable { onClick() },  // Aggiungi onClick
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


@Composable
fun CardBoxWithText(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.Black, fontSize = 16.sp)
        }
    }
}


@Composable
fun SetPinDialog(viewModel: AuthViewModel, onDismiss: () -> Unit) {
    val newPin = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Imposta PIN")
        },
        text = {
            OutlinedTextField(
                value = newPin.value,
                onValueChange = { newPin.value = it },
                label = { Text(text = "Nuovo PIN") }
            )
        },
        confirmButton = {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.setPin(newPin.value) // Imposta il nuovo PIN nel ViewModel
                    onDismiss()
                }
            }) {
                Text("Conferma")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}

@Composable
fun SetNFCDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Avvicina la tessera NFC",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth() // Per assicurarti che il testo sia centrato
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), // Spaziatura per migliorare l'aspetto
                horizontalAlignment = Alignment.CenterHorizontally // Centra tutto all'interno della colonna
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_nfc_24),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Centra il bottone
            ) {
                Button(onClick = onDismiss) {
                    Text("Chiudi")
                }
            }
        }
    )
}
