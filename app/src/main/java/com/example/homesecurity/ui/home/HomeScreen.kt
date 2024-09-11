package com.example.homesecurity.ui.home

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.Person
import com.example.homesecurity.BottomBarScreen
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R
import com.example.homesecurity.ui.record.RecordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HomeScreen(navController: NavController) {
    val peopleState = remember { mutableStateOf<List<Person>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val deviceConnected = remember { mutableStateOf(true) }
    val peopleLoading = remember { mutableStateOf(true) }
    val isButtonLoading = remember { mutableStateOf(false) }  // Nuovo stato di caricamento per il pulsante

    val buttonViewModel: HomeViewModel = viewModel()
    val recordViewModel: RecordViewModel = viewModel()
    val buttonView = buttonViewModel.buttonState.collectAsState()
    val recordArray = recordViewModel.records.collectAsState()

    val buttonColor = if (buttonView.value) colorResource(R.color.dark_green) else colorResource(R.color.dark_red)
    val buttonText = if (buttonView.value) "ON" else "OFF"

    LaunchedEffect(Unit) {
        val user = getUser(getCurrentUserId())

        if (user.thingsIds.isNullOrEmpty()) {
            deviceConnected.value = false
        }

        CoroutineScope(Dispatchers.IO).launch {
            peopleState.value = buttonViewModel.getHomePeople(user.id)
            peopleLoading.value = false
        }

        buttonViewModel.fetchButtonStateFromDatabase(user)
        recordViewModel.listRecords()
        isLoading.value = false
    }

    if (isLoading.value || peopleLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (!deviceConnected.value) {
            Box(contentAlignment = Alignment.Center) {
                Column {
                    Text(text = "Crea il tuo User e Connetti il tuo primo Device")
                    Button(onClick = {
                        navController.navigate(NotBottomBarPages.CreateUser.route)
                    }) {
                        Text("Registra un device")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxSize()
            ) {
                Row(
                    Modifier.background(colorResource(id = R.color.white)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow {
                        peopleState.value?.let { people ->
                            if (people.isNotEmpty()) {
                                items(people.size) { index ->
                                    val person = people[index]
                                    PersonBox(person)
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

                Spacer(modifier = Modifier.size(20.dp))

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            isButtonLoading.value = true

                            // Cambia lo stato del pulsante (ON/OFF)
                            changeButtonState(buttonViewModel)

                            // Torna al thread principale per aggiornare lo stato della UI
                            withContext(Dispatchers.Main) {
                                delay(1000)
                                isButtonLoading.value = false
                            }
                        }
                    },
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                ) {
                    if (isButtonLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = Color.White,
                            strokeWidth = 4.dp
                        )
                    } else {
                        Column {
                            Icon(
                                Icons.Default.PowerSettingsNew,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(75.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(buttonText, fontSize = 25.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    "Ultimi Record:",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 20.sp
                )

                LazyRow(
                    Modifier.background(colorResource(id = R.color.white))
                ) {
                    recordArray.value?.let { recordsList ->
                        items(recordsList.size) { index ->
                            val record = recordsList[index]
                            RecordBox(timestamp = record.timestamp, navController = navController, photos = record.photoBase64)
                        }
                    }
                }
                Text(
                    text = "vedi tutti i record >>",
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clickable {
                            navController.navigate(BottomBarScreen.Record.route)
                        },
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.blue_medium),
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}


@Composable
fun PersonBox(person: Person) {
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (person.photo != null && person.photo.isNotEmpty()) {
                // Decodifica la stringa Base64 in un array di byte
                val decodedBytes = Base64.decode(person.photo, Base64.DEFAULT)
                // Converti l'array di byte in un Bitmap
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                // Converte il Bitmap in ImageBitmap e lo mostra come immagine rotonda
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
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
                )
            }
            Text(
                text = person.name,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            if(person.inside){
                Text(text = "in casa",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                    )
            }
            else{
                Text(text = "fuori",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic)
            }
        }
    }
}


@Composable
fun RecordBox(timestamp: String, navController: NavController, photos: List<String>) {
    val formattedDate = remember {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date(timestamp.toLong() * 1000)
        sdf.format(date)
    }

    val formattedTime = remember {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp.toLong() * 1000)
        sdf.format(date)
    }

    val bitmap = remember(photos) {
        photos.firstOrNull()?.let { decodeBase64ToBitmap(it) }
    }

    Card(
        onClick = { navController.navigate(NotBottomBarPages.SingleRecord.withArgs(timestamp)) },
        modifier = Modifier.size(width = 100.dp, height = 200.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = formattedDate, textAlign = TextAlign.Center, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Record Image",
                    modifier = Modifier
                        .size(80.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.stock_image),
                    contentDescription = "Placeholder Image",
                    modifier = Modifier
                        .size(80.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = formattedTime, textAlign = TextAlign.Center, fontSize = 12.sp)
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}

