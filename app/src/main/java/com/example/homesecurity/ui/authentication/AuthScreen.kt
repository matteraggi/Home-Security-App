package com.example.homesecurity.ui.authentication

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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homesecurity.R

@Composable
fun AuthScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Parte alta con due box/card
        Row(
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardBoxWithIconAndNumber(iconResId = R.drawable.baseline_fingerprint_24, number = "1")
            CardBoxWithIconAndNumber(iconResId = R.drawable.baseline_nfc_24, number = "2")
        }

        // Box/Card che copre tutta la larghezza con due stringhe
        Card(
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Pin", color = Color.Black, fontSize = 16.sp)
                Text(text = "*************", color = Color.Black, fontSize = 16.sp)
            }
        }

        // Spazio verticale per distanziare
        Spacer(modifier = Modifier.height(24.dp))

        // Testo "Registra nuovo metodo di autenticazione"
        Text(
            text = "Registra nuovo metodo di autenticazione:",
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        // Spazio verticale per distanziare
        Spacer(modifier = Modifier.height(24.dp))

        // Parte inferiore con tre box/card in riga
        Row(
            modifier = Modifier.width(300.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardBoxWithIcon(iconResId = R.drawable.baseline_fingerprint_24)
            CardBoxWithIcon(iconResId = R.drawable.baseline_nfc_24)
            CardBoxWithText(text = "Cambia Pin")
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
fun CardBoxWithIcon(iconResId: Int) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp),
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
fun CardBoxWithText(text: String) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp),
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