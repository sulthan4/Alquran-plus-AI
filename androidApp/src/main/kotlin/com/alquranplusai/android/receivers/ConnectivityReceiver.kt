package com.alquranplusai.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConnectivityReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
        
        private val _networkState = MutableStateFlow(NetworkState.UNKNOWN)
        val networkState: StateFlow<NetworkState> = _networkState
        
        private val _isConnected = MutableStateFlow(false)
        val isConnected: StateFlow<Boolean> = _isConnected
        
        fun registerNetworkCallback(context: Context) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
                as ConnectivityManager
            
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _isConnected.value = true
                    updateNetworkState(context)
                }
                
                override fun onLost(network: Network) {
                    _isConnected.value = false
                    _networkState.value = NetworkState.DISCONNECTED
                }
                
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    updateNetworkState(context)
                }
            }
            
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
        
        private fun updateNetworkState(context: Context) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
                as ConnectivityManager
            
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            
            _networkState.value = when {
                capabilities == null -> NetworkState.DISCONNECTED
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.CELLULAR
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkState.ETHERNET
                else -> NetworkState.UNKNOWN
            }
        }
    }

    enum class NetworkState {
        WIFI,
        CELLULAR,
        ETHERNET,
        DISCONNECTED,
        UNKNOWN
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_CONNECTIVITY_CHANGE) {
            handleConnectivityChange(context)
        }
    }

    private fun handleConnectivityChange(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
            as ConnectivityManager
        
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        
        val isConnected = capabilities != null && 
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        
        _isConnected.value = isConnected
        
        if (isConnected) {
            val networkState = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.CELLULAR
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkState.ETHERNET
                else -> NetworkState.UNKNOWN
            }
            
            _networkState.value = networkState
            onNetworkAvailable(context, networkState)
        } else {
            _networkState.value = NetworkState.DISCONNECTED
            onNetworkLost(context)
        }
        
        saveConnectivityState(context, isConnected, _networkState.value)
    }

    private fun onNetworkAvailable(context: Context, networkState: NetworkState) {
        val intent = Intent("com.alquranplusai.android.NETWORK_AVAILABLE").apply {
            putExtra("network_type", networkState.name)
            putExtra("timestamp", System.currentTimeMillis())
        }
        context.sendBroadcast(intent)
    }

    private fun onNetworkLost(context: Context) {
        val intent = Intent("com.alquranplusai.android.NETWORK_LOST").apply {
            putExtra("timestamp", System.currentTimeMillis())
        }
        context.sendBroadcast(intent)
    }

    private fun saveConnectivityState(context: Context, isConnected: Boolean, networkState: NetworkState) {
        val prefs = context.getSharedPreferences("connectivity_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("is_connected", isConnected)
            putString("network_state", networkState.name)
            putLong("last_change_time", System.currentTimeMillis())
            apply()
        }
    }
}
