package com.example.homesecurity.ui.singlerecord

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SingleRecordScreen(timestamp: String) {
    val singleRecordViewModel = viewModel<SingleRecordViewModel>()
    val record = singleRecordViewModel.record.collectAsState()

    LaunchedEffect(key1 = Unit) {
        singleRecordViewModel.getRecord(timestamp)
    }

    val formattedDate = remember {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp.toLong() * 1000)
        sdf.format(date)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        if (record.value != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Record: $formattedDate",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(record.value?.photoBase64 ?: emptyList()) { photo ->
                    val photoBitmap = decodeBase64Image(photo)
                    if (photoBitmap != null) {
                        Image(
                            bitmap = photoBitmap,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Handle case where the image couldn't be decoded
                        Text(text = "Immagine non disponibile", modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        } else {
            Text(text = "Caricamento del record in corso...")
        }
    }
}

@Composable
fun decodeBase64Image(base64String: String): ImageBitmap? {
    return try {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
