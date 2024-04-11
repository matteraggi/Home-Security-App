package com.example.homesecurity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
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
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.ui.authenticator.enums.AuthenticatorStep
import com.amplifyframework.ui.authenticator.rememberAuthenticatorState
import com.amplifyframework.ui.authenticator.ui.Authenticator
import com.example.homesecurity.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            try {
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

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContent{
            val authenticatorState = rememberAuthenticatorState(
                initialStep = AuthenticatorStep.SignIn // Change to desired initial step (SignIn, SignUp, ResetPassword)
            )
            AppTheme() {
                Authenticator(
                    state = authenticatorState,
                    headerContent = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                        )
                    },
                    footerContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "© All Rights Reserved"
                            )
                        }
                    }
                ) { state ->
                    MainScreen(state);
                }
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

        /* Other default colors to override
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        */
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