package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.service.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val dateOfBirth: String = "August 20, 1987",
    val height: String = "",
    val weight: String = "",
    val conditions: String = "",
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }
    
    fun refreshProfile() {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Get current user from AccountService
                val currentUser = accountService.getCurrentAccount()
                if (currentUser != null) {
                    // Get user profile from Firestore
                    val userProfile = accountService.getProfile(currentUser.profileId)
                    if (userProfile != null) {
                        _uiState.value = _uiState.value.copy(
                            userName = if (userProfile.fullName.isNotEmpty()) userProfile.fullName else "User",
                            userEmail = currentUser.email,
                            dateOfBirth = if (userProfile.birthday > 0) "Birthday set" else "Not set",
                         //   healthInformation = if (userProfile.conditions.isNotEmpty()) userProfile.conditions else "No health conditions recorded",
                            avatarUrl = "",
                            isLoading = false
                        )
                    } else {
                        // If no profile exists, use basic user info
                        _uiState.value = _uiState.value.copy(
                            userName = currentUser.email.split("@").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "User",
                            userEmail = currentUser.email,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        userName = "Guest",
                        userEmail = "",
                        isLoading = false
                    )
                }
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
        height: String,
        weight: String,
        conditions: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: Update user profile in Firestore
                // val updatedProfile = UserProfile(
                //     name = name,
                //     dateOfBirth = dateOfBirth,
                //     height = height.toFloatOrNull() ?: 0f,
                //     weight = weight.toFloatOrNull() ?: 0f,
                //     conditions = conditions,
                //     email = _uiState.value.userEmail,
                //     avatarUrl = _uiState.value.avatarUrl
                // )
                // firestoreRepository.updateUserProfile(updatedProfile)

                _uiState.value = _uiState.value.copy(
                    userName = name,
                    dateOfBirth = dateOfBirth,
                    height = height,
                    weight = weight,
                    conditions = conditions,
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
                // Sign out from Firebase Auth
                accountService.signOut()
                
                // Clear local profile data immediately
                _uiState.value = ProfileUiState()
                
                // Force immediate auth state check by triggering profile change
                // This will be picked up by AuthViewModel's monitoring
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

    fun resetProfile() {
        _uiState.value = ProfileUiState(
            userName = "User",
            userEmail = "",
            dateOfBirth = "Not set",
            //healthInformation = "",
            avatarUrl = "",
            isLoading = false,
            errorMessage = null
        )
    }
}
