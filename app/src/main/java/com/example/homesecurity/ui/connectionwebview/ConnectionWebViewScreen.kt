package com.example.homesecurity.ui.connectionwebview

import android.annotation.SuppressLint
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ConnectionWebViewScreen(navController: NavController) {
    val webViewError = remember { mutableStateOf(false) }

    LaunchedEffect(webViewError.value) {
        if (webViewError.value) {
            // Torna alla pagina precedente quando si verifica un errore di rete
            navController.popBackStack()
        }
    }

    AndroidView(factory = {
        WebView(it).apply {
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    // Gestisce l'errore di rete
                    webViewError.value = true
                }
            }
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl("http://192.168.4.1")
        }
    })
}