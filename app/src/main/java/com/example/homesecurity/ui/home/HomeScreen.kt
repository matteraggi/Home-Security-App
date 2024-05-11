package com.example.homesecurity.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.User
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.NotificationService
import com.example.homesecurity.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


var userId: String? = null

@Composable
fun HomeScreen(navController: NavController) {
    val people = remember {
        mutableListOf(
            PeopleBox(
                name = "Matteo",
                image = "immagine",
                inside = true
            ),
            PeopleBox(
                name = "Madre",
                image = "immagine",
                inside = true
            ),
            PeopleBox(
                name = "Padre",
                image = "immagine",
                inside = true
            ),
            PeopleBox(
                name = "Vale",
                image = "immagine",
                inside = true
            )
        )
    }

    val buttonViewModel = viewModel<HomeViewModel>()
    val recordViewModel = viewModel<RecordViewModel>()
    val buttonView = buttonViewModel.buttonState.collectAsState()
    val recordArray = recordViewModel.records.collectAsState()
    val service = NotificationService(LocalContext.current)

    val buttonColor = if(buttonView.value) colorResource(R.color.dark_red) else colorResource(R.color.dark_green)


    LaunchedEffect(key1 = Unit) {
        buttonViewModel.fetchButtonStateFromDatabase()
        recordViewModel.listRecords()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {

        // Testo chi è in casa
        Text(
            "Chi è in casa:",
            modifier = Modifier.padding(vertical = 5.dp),
            fontSize = 20.sp
        )

        // People RecyclerView
        LazyRow(
            Modifier.background(colorResource(id = R.color.white))
        ){
            items(people.size) { index ->
                val person = people[index]
                PersonBox(person)
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        // Alarm Button
        Button(
            onClick = { CoroutineScope(Dispatchers.IO).launch {
                changeButtonState(buttonViewModel)
            } },
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(buttonColor),
        ) {
            Column{
                Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterHorizontally))
                Text(if (buttonView.value) "OFF" else "ON" ,fontSize = 25.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

        Spacer(modifier = Modifier.size(20.dp))
        
        Button(onClick = {
            service.showNotification()
        }) {
            Text("show notification!")
        }

        Text(
            "Ultimi Record:",
            modifier = Modifier.padding(vertical = 10.dp),
            fontSize = 20.sp
        )

        // Record RecyclerView
        LazyRow(
            Modifier.background(colorResource(id = R.color.white))
        ){
            recordArray.value?.let { recordsList ->
                items(recordsList.size) { index ->
                    val record = recordsList[index]
                    RecordBox(text = record.timestamp, navController = navController)
                }
            }
        }
    }
}

@Composable
fun PersonBox(person: PeopleBox) {
    val (inside, setInside) = remember { mutableStateOf(person.inside) }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                setInside(!inside)
            }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(
                painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = null,
                tint = if(inside) Color.Black else Color.Gray,
                modifier = Modifier.size(80.dp))
            Text(text = person.name, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun RecordBox(text: String, navController: NavController) {
    Card(
        onClick = { navController.navigate(NotBottomBarPages.SingleRecord.withArgs(text)) },
        modifier = Modifier.size(width = 120.dp, height = 70.dp),
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Text(text = text, textAlign = TextAlign.Center)
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}
suspend fun changeButtonState(viewModel: HomeViewModel) {
    val id = getCurrentUserId()
    if (id.isEmpty()) {
        Log.e("Amplify", "ID utente nullo o vuoto")
        return
    }

    val newAlarmValue = !viewModel.buttonState.value

    val updatedUser = User.builder().alarm(newAlarmValue).id(id).build()

    try {
        Amplify.API.mutate(
            ModelMutation.update(updatedUser),
            { _ ->
                Log.i("Amplify", "Allarme aggiornato: $updatedUser")
                viewModel.changeButtonUi()
            },
            { error ->
                Log.e("Amplify", "Errore durante l'aggiornamento dell'utente: $error")
            }
        )
    } catch (e: Exception) {
        Log.e("Amplify", "Errore durante l'aggiornamento dell'utente: $e")
    }
}