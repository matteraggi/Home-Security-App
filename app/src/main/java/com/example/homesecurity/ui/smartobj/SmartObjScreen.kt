package com.example.homesecurity.ui.smartobj

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R

@Composable
fun SmartObjScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Testo in grassetto
        Text(
            text = "Vuoi connettere un nuovo dispositivo?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Testo in grigio
        Text(
            text = "scegli il tipo di oggetto e segui la procedura guidata",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Box con opzioni
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            DeviceOption(
                label = "Sensore",
                icon = R.drawable.baseline_sensors_24,
                onClick = {
                    navController.navigate(NotBottomBarPages.WifiList.route)
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            DeviceOption(
                label = "Camera",
                icon = R.drawable.baseline_camera_indoor_24,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(24.dp))
            DeviceOption(
                label = "Sensore Porta",
                icon = R.drawable.baseline_sensor_door_24,
                onClick = {}
            )
        }
    }
}

@Composable
fun DeviceOption(label: String, icon: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(
                horizontal = 12.dp
            ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 28.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color.Black
            )
        }
    }
}
