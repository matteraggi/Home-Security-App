package com.example.homesecurity.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homesecurity.R

@Composable
fun HomeScreen() {
    val people = remember {
        mutableListOf(
            PeopleBox(
                name = "Matteo",
                image = "immagine",
                description = "sono io"
            ),
            PeopleBox(
                name = "Madre",
                image = "immagine",
                description = "è lei"
            )
        )
    }

    val records = remember {
        mutableListOf(
            RecordEl(
                timestamp = "237284364344"
            ),
            RecordEl(
                timestamp = "237284364344"
            ),
            RecordEl(
                timestamp = "237284364344"
            ),
            RecordEl(
                timestamp = "237284364344"
            ),
            RecordEl(
                timestamp = "237284364344"
            )

        )
    }

    val (isAlarmOn, setIsAlarmOn) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxSize()
    ) {

        // Testo chi è in casa
        Text(
            "Chi è in casa:",
            modifier = Modifier.padding(vertical = 10.dp),
            fontSize = 20.sp
        )

        // People RecyclerView
        LazyRow(
            Modifier.background(colorResource(id = R.color.white))
        ){
            items(people.size) { index ->
                val person = people[index]
                PersonBox(text = person.name)
            }
        }

        Spacer(modifier = Modifier.size(40.dp))

        // Alarm Button
        Button(
            onClick = { setIsAlarmOn(!isAlarmOn) },
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
        ) {
            Column{
                Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally))
                Text(if (isAlarmOn) "OFF" else "ON",fontSize = 35.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
        Text(
            "L'allarme è spento",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
        )

        Spacer(modifier = Modifier.size(40.dp))

        Text(
            "Record:",
            modifier = Modifier.padding(vertical = 10.dp),
            fontSize = 20.sp
        )

        // Record RecyclerView
        LazyRow(
            Modifier.background(colorResource(id = R.color.white))
        ){
            items(records.size) { index ->
                val record = records[index]
                RecordBox(text = record.timestamp)
            }
        }
    }
}

@Composable
fun PersonBox(text: String) {
    Box(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(80.dp))
            Text(text = text, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun RecordBox(text: String) {
    Card(
        modifier = Modifier.size(width = 120.dp, height = 90.dp),
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Text(text = text, textAlign = TextAlign.Center)
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}


@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}