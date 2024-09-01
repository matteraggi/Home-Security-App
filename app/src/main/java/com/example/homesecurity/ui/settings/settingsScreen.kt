package com.example.homesecurity.ui.settings

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.Person
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R
import com.example.homesecurity.ui.home.getCurrentUserId

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val peopleState = remember { mutableStateOf<List<Person>?>(null) }
    val selectedPersonIdState = remember { mutableStateOf(getSelectedPersonId(context)) }
    val settingsViewModel: SettingsViewModel = viewModel()
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        peopleState.value = settingsViewModel.getHomePeople(getCurrentUserId())
        isLoading.value = false
    }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    text = "Vuoi impostare una nuova posizione per la tua casa?"
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    color = Color.Gray,
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                    text = "Quando ti allontanerai dalla posizione, verrai automaticamente rilevato come fuori dalla casa, e viceversa"
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue_medium)),
                    onClick = { navController.navigate(NotBottomBarPages.Map.route) }
                ) {
                    Text("riposiziona geofencing")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    text = "Seleziona il tuo utente:"
                )

                if (isLoading.value) {
                    // Mostra la rotella di caricamento
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingSpinner()
                    }
                } else {
                    // Mostra la lista delle persone
                    LazyRow {
                        peopleState.value?.let { people ->
                            if (people.isNotEmpty()) {
                                items(people.size) { index ->
                                    val person = people[index]
                                    val isSelected = person.id == selectedPersonIdState.value

                                    PersonBox(
                                        person = person,
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedPersonIdState.value = person.id
                                            saveSelectedPersonId(context, person.id)
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Spacer(modifier = Modifier.height(30.dp))
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(
                                            colorResource(id = R.color.blue_medium),
                                            CircleShape
                                        )
                                        .clickable { navController.navigate(NotBottomBarPages.CreateNewUser.route) }
                                ) {
                                    Text(
                                        text = "+",
                                        fontSize = 40.sp,
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    text = "Vuoi eliminare il tuo Account?"
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    color = Color.Gray,
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                    text = "Quando eliminerai il tuo Account perderai qualsiasi informazione. Creandone uno nuovo dovrai collegare nuovamente i dispositivi."
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Controlla se `user` è non-null prima di usare il pulsante elimina account
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_red)),
                    onClick = {
                        settingsViewModel.deleteAccount()
                    },
                ) {
                    Text("elimina account")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    text = "Entra con un altro account:"
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue_medium)),
                    onClick = {
                        settingsViewModel.logOut()
                    },
                ) {
                    Text("log out")
                }
            }
        }
    )
}

@Composable
fun LoadingSpinner() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        androidx.compose.material3.CircularProgressIndicator()
    }
}


@Composable
fun PersonBox(person: Person, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (person.photo != null && person.photo.isNotEmpty()) {
                val decodedBytes = Base64.decode(person.photo, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 5.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 5.dp else 0.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
            Text(
                text = person.name,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



fun saveSelectedPersonId(context: Context, personId: String) {
    val sharedPref = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("selected_person_id", personId)
        apply()
    }
}

fun getSelectedPersonId(context: Context): String? {
    val sharedPref = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    return sharedPref.getString("selected_person_id", null)
}


const val REQUEST_LOCATION_PERMISSION = 1
