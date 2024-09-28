package com.example.homesecurity

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.ui.authenticator.enums.AuthenticatorStep
import com.amplifyframework.ui.authenticator.rememberAuthenticatorState
import com.amplifyframework.ui.authenticator.ui.Authenticator
import com.example.homesecurity.ui.authentication.AuthViewModel
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import com.example.homesecurity.ui.home.registerNFC
import com.example.homesecurity.ui.locationmap.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // schermata auth tutto alla stessa larghezza

    // Modifica il callback per gestire la richiesta del permesso di accesso alla posizione in background
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("Permessi", "${it.key} = ${it.value}")
                if (!it.value) {
                    Log.e("Permessi", "Permesso negato: ${it.key}")
                }
            }

            // Se i permessi per la posizione in primo piano sono stati concessi, richiedi il permesso per la posizione in background
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {

                // Verifica se il permesso per la posizione in background è già stato concesso
                if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Mostra una spiegazione e poi richiedi il permesso
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        // Mostra una spiegazione
                        Toast.makeText(this, "Il permesso di accesso alla posizione in background è necessario per alcune funzionalità dell'app.", Toast.LENGTH_LONG).show()
                    }
                    // Richiedi il permesso di accesso alla posizione in background
                    requestBackgroundLocationPermission()
                }
            }
        }

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var nfcAdapter: NfcAdapter? = null

    private var isAmplifyConfigured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Log.d("Permessi", "Calling requestPermissions() in onCreate")
        requestPermissions()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Amplify.addPlugin(AWSApiPlugin())
                Amplify.addPlugin(AWSCognitoAuthPlugin())
                Amplify.configure(applicationContext)
                isAmplifyConfigured = true
                Log.i("MyAmplifyApp", "Initialized Amplify")
            } catch (error: AmplifyException) {
                Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
            }
            try {
                val session = Amplify.Auth.fetchAuthSession()
                Log.i("AmplifyQuickstart", "Auth session = $session")
            } catch (error: AuthException) {
                Log.e("AmplifyQuickstart", "Failed to fetch auth session", error)
            }
        }

        val sharedPreferences = getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE)
        val geofenceId = sharedPreferences.getString("geofenceId", null)
        val savedLat = sharedPreferences.getFloat("geofenceLat", 0f)
        val savedLon = sharedPreferences.getFloat("geofenceLon", 0f)

        mapViewModel.initialize(this)

        // Ripristina il Geofence se c'è già un Geofence salvato
        if (geofenceId != null && savedLat != 0f && savedLon != 0f) {
            mapViewModel.updateGeofence(this, savedLat.toDouble(), savedLon.toDouble())
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // NFC non supportato sul dispositivo
            Toast.makeText(this, "NFC not supported on this device", Toast.LENGTH_SHORT).show()
        } else if (!nfcAdapter!!.isEnabled) {
            // NFC non abilitato
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show()
        }

        initNFCFunction()

        setContent {
            val authenticatorState = rememberAuthenticatorState(
                initialStep = AuthenticatorStep.SignIn // Change to desired initial step (SignIn, SignUp, ResetPassword)
            )
            AppTheme {
                Authenticator(
                    state = authenticatorState,
                    headerContent = {
                        Box(
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    footerContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("© All Rights Reserved")
                        }
                    }
                ) { state ->
                    if (state.step == AuthenticatorStep.SignedIn) {
                        onLoginSuccess()
                    }
                    MainScreen(state)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            while (!isAmplifyConfigured) {
                // Aspetta che Amplify sia configurato
                delay(100)  // Attendi un po' prima di riprovare
            }

            try {
                val userId = getCurrentUserId()
                val user = getUser(userId)

                if (user.nfc.isNullOrEmpty()) {
                    runOnUiThread {
                        enableForegroundDispatch()
                    }
                }
            } catch (error: AuthException) {
                Log.e("onResume", "Failed to get current user", error)
            }
        }

        if (nfcAdapter?.isEnabled == true) {
            initService()
        }
    }



    override fun onPause() {
        super.onPause()
        disableForegroundDispatch()
    }

    fun enableForegroundDispatch() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter != null) {
            if (nfcAdapter.isEnabled) {
                Log.d("NFC", "NFC is enabled")
                val pendingIntent = PendingIntent.getActivity(
                    this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_MUTABLE
                )
                val intentFiltersArray = arrayOf(
                    IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                    IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                    IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
                )
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null)
                Log.d("NFC", "NFC foreground dispatch enabled")
            } else {
                Log.d("NFC", "NFC is not enabled")
                Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("NFC", "NFC is not supported on this device")
            Toast.makeText(this, "NFC not supported on this device", Toast.LENGTH_SHORT).show()
        }
    }


    fun disableForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("NFC", "onNewIntent called")
        handleNfcIntent(intent)
    }

    @Suppress("DEPRECATION")
    private fun handleNfcIntent(intent: Intent) {
        val authViewModel: AuthViewModel by viewModels()

        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED ||
            intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            // Recupera il tag dal Intent
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                // Recupera l'ID del tag NFC
                val tagId = tag.id
                val tagIdHex = tagId.joinToString(separator = "") { String.format("%02X", it) }
                Log.d("NFC", "Tag ID: $tagIdHex")

                CoroutineScope(Dispatchers.IO).launch {
                    registerNFC(tagIdHex,
                        onSuccess = {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Tessera NFC registrata con successo", Toast.LENGTH_SHORT).show()
                                authViewModel.dismissNFCDialog()
                            }
                        },
                        onError = { errorMessage ->
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Errore: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                val ndef = Ndef.get(tag)
                if (ndef != null) {
                    ndef.connect()
                    val ndefMessage = ndef.ndefMessage
                    if (ndefMessage != null) {
                        val text = ndefMessage.records[0].payload.toString(Charsets.UTF_8)
                        Log.d("NFC", "NDEF Message: $text")
                    } else {
                        Log.d("NFC", "No NDEF messages found")
                    }
                    ndef.close()
                } else {
                    Log.d("NFC", "NDEF not supported on this tag")
                }
            } else {
                Log.d("NFC", "No tag found in intent")
            }
        }
    }




    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE)
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE)
        permissions.add(Manifest.permission.CHANGE_NETWORK_STATE)
        permissions.add(Manifest.permission.INTERNET)
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        permissions.add(Manifest.permission.NFC)

        val permissionsArray = permissions.toTypedArray()

        Log.d("Permessi", "Richiesta di permessi: ${permissionsArray.joinToString()}")

        requestPermissionsLauncher.launch(permissionsArray)
    }

    private fun requestBackgroundLocationPermission() {
        val permissions = mutableListOf<String>()

        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

        val permissionsArray = permissions.toTypedArray()

        requestPermissionsLauncher.launch(permissionsArray)
    }


    private fun onLoginSuccess() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("onLoginSuccess", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.i("onLoginSuccess", token)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userId = getCurrentUserId()
                    val user = getUser(userId)

                    // Controlla se il campo nfc è vuoto
                    if (user.nfc.isNullOrEmpty()) {
                        Log.d("NFC", "Il campo NFC dell'utente è vuoto. Attivazione della ricerca NFC.")
                        // Attiva la ricerca NFC
                        runOnUiThread {
                            enableForegroundDispatch()
                        }
                    } else {
                        Log.d("NFC", "Il campo NFC dell'utente non è vuoto. La ricerca NFC non è attivata.")
                    }

                    checkAndUpdateDeviceId(userId, token)
                } catch (error: AuthException) {
                    Log.e("onLoginSuccess", "Failed to get current user", error)
                }
            }
        })
    }


    private fun checkAndUpdateDeviceId(userId: String, deviceId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = getUser(userId)
                if (!user.deviceIds.contains(deviceId)) {
                    user.deviceIds.add(deviceId)
                    com.amplifyframework.core.Amplify.API.mutate(
                        ModelMutation.update(user),
                        { _ ->
                            Log.i("checkAndUpdateDeviceId", "User updated")
                        },
                        { error ->
                            Log.e(
                                "checkAndUpdateDeviceId",
                                "Failed to update user: $error"
                            )
                        }
                    )
                }
            } catch (error: AuthException) {
                Log.e("checkAndUpdateDeviceId", "Failed to get current user", error)
            }
        }
    }

    private fun initNFCFunction() {
        if (supportNfcHceFeature()) {
            initService()
        }
    }

    private fun supportNfcHceFeature() =
        checkNFCEnable() && packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)

    private fun checkNFCEnable(): Boolean {
        return if (nfcAdapter == null) {
            false
        } else {
            nfcAdapter?.isEnabled == true
        }
    }

    private fun initService() {
        val intent = Intent(this@MainActivity, MyHostApduService::class.java)
        intent.putExtra("ndefMessage", "id")
        startService(intent)
    }
}

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val darkColorScheme = darkColorScheme(
        primary = colorResource(id = R.color.blue_medium),
        secondary = colorResource(id = R.color.blue_high),
        tertiary = colorResource(id = R.color.white)
    )

    val lightColorScheme = lightColorScheme(
        primary = colorResource(id = R.color.blue_medium),
        secondary = colorResource(id = R.color.blue_high),
        tertiary = colorResource(id = R.color.white)
    )
    val colors = if (!useDarkTheme) {
        lightColorScheme
    } else {
        darkColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}