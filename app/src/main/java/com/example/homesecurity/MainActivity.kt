package com.example.homesecurity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import com.example.homesecurity.ui.home.getCurrentUserId
import com.example.homesecurity.ui.home.getUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("Permessi", "${it.key} = ${it.value}")
                if (!it.value) {
                    Log.e("Permessi", "Permesso negato: ${it.key}")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Permessi", "Calling requestPermissions() in onCreate")
        requestPermissions()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Amplify.addPlugin(AWSApiPlugin())
                Amplify.addPlugin(AWSCognitoAuthPlugin())
                Amplify.configure(applicationContext)
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

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE)
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE)
        permissions.add(Manifest.permission.CHANGE_NETWORK_STATE)
        permissions.add(Manifest.permission.INTERNET)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val permissionsArray = permissions.toTypedArray()

        Log.d("Permessi", "Richiesta di permessi: ${permissionsArray.joinToString()}")

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