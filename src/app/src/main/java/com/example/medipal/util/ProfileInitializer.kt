package com.example.medipal.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileInitializer : KoinComponent {
    
    private val profileRepositoryManager: ProfileRepositoryManager by inject()
    
    /**
     * Initialize the app with a default profile.
     * This should be called early in the app lifecycle.
     */
    fun initializeWithDefaultProfile() {
        // Use the same default profile ID as in RepositoryModule
        val defaultProfileId = "default-profile"
        profileRepositoryManager.setCurrentProfile(defaultProfileId)
    }
    
    /**
     * Initialize the app with a specific profile.
     * This should be called after user authentication.
     */
    fun initializeWithProfile(profileId: String) {
        profileRepositoryManager.setCurrentProfile(profileId)
    }
    
    /**
     * Get the current profile ID.
     */
    fun getCurrentProfileId(): String {
        return profileRepositoryManager.getCurrentProfileId()
    }
}
