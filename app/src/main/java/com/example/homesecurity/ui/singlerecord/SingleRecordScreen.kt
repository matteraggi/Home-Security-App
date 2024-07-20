package com.example.homesecurity.ui.singlerecord

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SingleRecordScreen(timestamp: String?) {

    val formattedDate = remember {
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault())
        val date = Date(timestamp!!.toLong() * 1000)
        sdf.format(date)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(text = "Record: $formattedDate")
    }
}