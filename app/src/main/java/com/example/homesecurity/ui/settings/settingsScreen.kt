package com.example.homesecurity.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homesecurity.NotBottomBarPages
import com.example.homesecurity.R

@Composable
fun SettingsScreen(navController: NavController) {

    val settingsViewModel: SettingsViewModel = viewModel()

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "Vuoi impostare una nuova posizione per la tua casa?"
                )
                Text(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    text = "Quando ti allontanerai dalla posizione, verrai automaticamente rilevato come fuori dalla casa, e viceversa"
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue_medium)),
                    onClick = { navController.navigate(NotBottomBarPages.Map.route) }
                ) {
                    Text("riposiziona geofencing")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "Vuoi eliminare il tuo Account?"
                )
                Text(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    text = "Quando eliminerai il tuo Account perderai qualsiasi informazione. Creandone uno nuovo dovrai collegare nuovamente i dispositivi."
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Controlla se `user` è non-null prima di usare il pulsante elimina account
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_red)),
                    onClick = {
                        settingsViewModel.deleteAccount()
                    },
                ) {
                    Text("elimina account")
                }
            }
        }
    )
}



const val REQUEST_LOCATION_PERMISSION = 1
