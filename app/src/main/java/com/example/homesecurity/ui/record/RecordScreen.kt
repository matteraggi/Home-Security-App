package com.example.homesecurity.ui.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.ui.home.RecordEl

@Composable
fun RecordScreen(navController: NavController) {
    val records = remember {
        mutableListOf(
            RecordEl(
                timestamp = "1"
            ),
            RecordEl(
                timestamp = "2"
            ),
            RecordEl(
                timestamp = "3"
            ),
            RecordEl(
                timestamp = "4"
            ),
            RecordEl(
                timestamp = "5"
            )

        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        LazyColumn(){
            items(records.size) { index ->
                val record = records[index]
                RecordLine(text = record.timestamp, navController)
            }
        }
    }
}

@Composable
fun RecordLine(text: String, navController: NavController) {
    Card(
        onClick = {navController.navigate(NotBottomBarPages.SingleRecord.withArgs(text)) },
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Text(text = text, textAlign = TextAlign.Center)
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}