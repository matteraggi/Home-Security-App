package com.example.homesecurity

import android.Manifest
import android.os.Build
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
import androidx.core.app.ActivityCompat
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.ui.authenticator.enums.AuthenticatorStep
import com.amplifyframework.ui.authenticator.rememberAuthenticatorState
import com.amplifyframework.ui.authenticator.ui.Authenticator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

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

        /*
        Create User:

        val model = User.builder()
    		.email("test12346789@testemailtestemail.com")
		    .password("Lorem ipsum dolor sit amet")
		    .HomeUser(/* Provide a HomeUser instance here */)
            .build()

        Amplify.API.mutate(ModelMutation.create(model),
            { Log.i("MyAmplifyApp", "User with id: ${it.data.id}") }
            { Log.e("MyAmplifyApp", "Create failed", it) }
        )
        */

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
                    MainScreen(state)
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