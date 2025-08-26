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
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SyncManager : KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isMonitoring = false

    // Repos that need syncing
    private val hybridRepositories = mutableListOf<HybridRepositoryImpl<*>>()

    // Profile manager reference (injected via Koin)
    private val profileRepositoryManager: ProfileRepositoryManager by inject()

    /** Register repositories that should sync */
    fun register(vararg repos: HybridRepositoryImpl<*>) {
        repos.filterNotNull().forEach { repo ->
            hybridRepositories.add(repo)

            // Start remote listener if Firestore-backed
            repo.startRemoteListener(scope)
        }
    }

    /** Start monitoring network and profile changes */
    fun startMonitoring(context: Context) {
        if (isMonitoring) return
        isMonitoring = true

        startNetworkMonitoring(context)
        startProfileMonitoring()
    }

    /** Start monitoring network changes */
    private fun startNetworkMonitoring(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Small delay to avoid rapid calls on flaky connections
                scope.launch {
                    delay(500)
                    hybridRepositories.forEach { it.syncAll() }
                }
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

    /** Start monitoring profile changes */
    private fun startProfileMonitoring() {
        scope.launch {
            profileRepositoryManager.currentProfileId.collect { newProfileId ->
                hybridRepositories.forEach { it.syncAll() }
            }
        }
    }

    /** Manually trigger a full sync for all registered repos */
    suspend fun syncAllNow() {
        hybridRepositories.forEach { it.syncAll() }
    }
}

