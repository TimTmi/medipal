package com.example.medipal.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.example.medipal.data.repository.HybridRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

object SyncManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isMonitoring = false

    // Repos that need syncing
    private val hybridRepositories = mutableListOf<HybridRepositoryImpl<*>>()

    // Register repositories that should sync
    fun register(vararg repos: HybridRepositoryImpl<*>) {
        hybridRepositories.addAll(repos)
    }

    // Start monitoring network
    fun startMonitoring(context: Context) {
        if (isMonitoring) return
        isMonitoring = true

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Trigger sync for all registered repos
                hybridRepositories.forEach { repo ->
                    scope.launch {
                        delay(500)
                        repo.syncAll()
                    }
                }
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)
    }
}
