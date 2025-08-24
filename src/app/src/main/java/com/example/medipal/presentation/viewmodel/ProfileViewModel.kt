package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userName: String = "Tom Conor",
    val userEmail: String = "",
    val dateOfBirth: String = "August 20, 1987",
    val healthInformation: String = "",
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Load user profile from Firestore
                // val userProfile = firestoreRepository.getCurrentUserProfile()
                // _uiState.value = _uiState.value.copy(
                //     userName = userProfile.name,
                //     userEmail = userProfile.email,
                //     dateOfBirth = userProfile.dateOfBirth,
                //     healthInformation = userProfile.healthInformation,
                //     avatarUrl = userProfile.avatarUrl,
                //     isLoading = false
                // )
                
                // For now, use default values
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load profile: ${e.message}"
                )
            }
        }
    }

    fun updateProfile(
        name: String,
        dateOfBirth: String,
        healthInformation: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Update user profile in Firestore
                // val updatedProfile = UserProfile(
                //     name = name,
                //     dateOfBirth = dateOfBirth,
                //     healthInformation = healthInformation,
                //     email = _uiState.value.userEmail,
                //     avatarUrl = _uiState.value.avatarUrl
                // )
                // firestoreRepository.updateUserProfile(updatedProfile)
                
                _uiState.value = _uiState.value.copy(
                    userName = name,
                    dateOfBirth = dateOfBirth,
                    healthInformation = healthInformation,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to update profile: ${e.message}"
                )
            }
        }
    }

    fun updateAvatar(avatarUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Update avatar in Firestore
                // firestoreRepository.updateUserAvatar(avatarUrl)
                
                _uiState.value = _uiState.value.copy(
                    avatarUrl = avatarUrl,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to update avatar: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // TODO: Sign out from Firebase Auth
                // firebaseAuth.signOut()
                
                // Clear local data if needed
                _uiState.value = ProfileUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to logout: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
