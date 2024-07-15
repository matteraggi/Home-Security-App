package com.example.homesecurity.ui.wifilist

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class WifiNetwork(val ssid: String, val bssid: String)

class WifiListViewModel(application: Application) : AndroidViewModel(application) {

    private val wifiManager: WifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val _wifiList = MutableStateFlow<List<WifiNetwork>>(emptyList())
    val wifiList: StateFlow<List<WifiNetwork>> = _wifiList

    private val _arrowButtonEnabled = MutableStateFlow(false)
    val arrowButtonEnabled: StateFlow<Boolean> = _arrowButtonEnabled

    init {
        startScan()
    }

    fun startScan() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("wifi", "start wifi scan")
            try {
                val scanResults = wifiManager.scanResults
                    .map { WifiNetwork(it.SSID, it.BSSID) }
                    .distinctBy { it.ssid }

                _wifiList.value = scanResults
                Log.i("wifi", scanResults.toString())

            } catch (e: SecurityException) {
                scanFailure()
            }
        } else {
            // Handle the case where permissions are not granted
            scanFailure()
        }
    }

    fun connectToWifi(ssid: String, password: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectToWifiWithNetworkSpecifier(ssid, password)
        } else {
            connectToWifiLegacy(ssid, password)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectToWifiWithNetworkSpecifier(ssid: String, password: String) {
        val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .build()

        val wifiRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()

        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(
            wifiRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.i("WifiViewModel", "Connected to WiFi: $ssid")
                    connectivityManager.bindProcessToNetwork(network)
                    _arrowButtonEnabled.value = true
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.e("WifiViewModel", "Failed to connect to WiFi: $ssid")
                    _arrowButtonEnabled.value = false

                }
            })
    }

    private fun connectToWifiLegacy(ssid: String, password: String) {
        val wifiConfig = WifiConfiguration().apply {
            SSID = String.format("\"%s\"", ssid)
            preSharedKey = String.format("\"%s\"", password)
        }

        val netId = wifiManager.addNetwork(wifiConfig)
        Log.i("WifiViewModel", netId.toString())
        if (netId != -1) {
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect()
            _arrowButtonEnabled.value = true
            Log.i("WifiViewModel", "connection ok")
        } else {
            _arrowButtonEnabled.value = false
            Log.e("WifiViewModel", "Failed to add network configuration")
        }
    }

    private fun scanFailure() {
        _wifiList.value = emptyList()
    }
}
