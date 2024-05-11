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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.ui.home.RecordViewModel

@Composable
fun RecordScreen(navController: NavController) {

    val recordViewModel = viewModel<RecordViewModel>()
    val recordArray = recordViewModel.records.collectAsState()

    LaunchedEffect(key1 = Unit) {
        recordViewModel.listRecords()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        LazyColumn {
            recordArray.value?.let { recordsList ->
                items(recordsList.size) { index ->
                    val record = recordsList[index]
                    RecordLine(text = record.timestamp, navController = navController)
                }
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