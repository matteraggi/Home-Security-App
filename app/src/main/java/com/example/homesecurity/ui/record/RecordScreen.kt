package com.example.homesecurity.ui.record

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                    RecordLine(timestamp = record.timestamp, navController = navController, photos = record.photo)
                }
            }
        }
    }
}

@Composable
fun RecordLine(timestamp: String, navController: NavController, photos: List<String>) {
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

    Card(
        onClick = { navController.navigate(NotBottomBarPages.SingleRecord.withArgs(timestamp)) },
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formattedTime, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                Text(text = formattedDate, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                if (photos.isNotEmpty()) {
                    items(photos.size) { photo ->
                        Image(
                            painter = painterResource(id = photo.toInt()), // Assuming photos are resource IDs
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                } else {
                    items(4) {
                        Image(
                            painter = painterResource(id = R.drawable.stock_image), // Replace with your stock image resource ID
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}
