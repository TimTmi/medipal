package com.example.medipal.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.service.AccountService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val dateOfBirth: String = "",
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
    
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var currentProfileId: String? = null

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val currentUser = accountService.getCurrentAccount()
                if (currentUser != null) {
                    currentProfileId = currentUser.profileId
                    
                    // Load profile from Firestore
                    val profileDoc = firestore.collection("profiles")
                        .document(currentUser.profileId)
                        .get()
                        .await()
                    
                    if (profileDoc.exists()) {
                        val profile = profileDoc.toObject(Profile::class.java)
                        profile?.let {
                            val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                            val birthdayString = if (it.birthday > 0) {
                                dateFormatter.format(Date(it.birthday))
                            } else "Not set"
                            
                            _uiState.value = _uiState.value.copy(
                                userName = it.fullName.ifEmpty { currentUser.email.split("@").first() },
                                userEmail = currentUser.email,
                                dateOfBirth = birthdayString,
                                height = if (it.height > 0) it.height.toString() else "",
                                weight = if (it.weight > 0) it.weight.toString() else "",
                                conditions = it.conditions,
                                avatarUrl = it.avatarUrl,
                                isLoading = false
                            )
                        }
                    } else {
                        // Create new profile if doesn't exist
                        val newProfile = Profile(
                            id = currentUser.profileId,
                            fullName = currentUser.email.split("@").first(),
                            avatarUrl = ""
                        )
                        
                        firestore.collection("profiles")
                            .document(currentUser.profileId)
                            .set(newProfile)
                            .await()
                        
                        _uiState.value = _uiState.value.copy(
                            userName = newProfile.fullName,
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
                val profileId = currentProfileId ?: return@launch
                
                // Parse date of birth to timestamp
                val birthdayTimestamp = if (dateOfBirth.isNotEmpty() && dateOfBirth != "Not set") {
                    try {
                        val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                        dateFormatter.parse(dateOfBirth)?.time ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                } else 0L
                
                val updatedProfile = mapOf(
                    "fullName" to name,
                    "birthday" to birthdayTimestamp,
                    "height" to (height.toFloatOrNull() ?: 0f),
                    "weight" to (weight.toFloatOrNull() ?: 0f),
                    "conditions" to conditions,
                    "avatarUrl" to _uiState.value.avatarUrl,
                    "updatedAt" to System.currentTimeMillis()
                )

                firestore.collection("profiles")
                    .document(profileId)
                    .update(updatedProfile)
                    .await()

                _uiState.value = _uiState.value.copy(
                    userName = name,
                    dateOfBirth = dateOfBirth,
                    height = height,
                    weight = weight,
                    conditions = conditions,
                    isLoading = false
                )
                
                // Reload profile to ensure consistency
                loadUserProfile()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to update profile: ${e.message}"
                )
            }
        }
    }

    fun uploadAvatar(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val profileId = currentProfileId ?: return@launch
                Log.d("ProfileViewModel", "Starting avatar upload for profileId: $profileId")
                
                // Create storage reference
                val storageRef = storage.reference
                val avatarRef = storageRef.child("avatars/$profileId.jpg")
                Log.d("ProfileViewModel", "Storage reference created: avatars/$profileId.jpg")
                
                // Upload image
                val uploadTask = avatarRef.putFile(imageUri).await()
                Log.d("ProfileViewModel", "Image uploaded successfully")
                
                val downloadUrl = avatarRef.downloadUrl.await()
                Log.d("ProfileViewModel", "Download URL obtained: $downloadUrl")
                
                // Update profile with new avatar URL
                firestore.collection("profiles")
                    .document(profileId)
                    .update(
                        mapOf(
                            "avatarUrl" to downloadUrl.toString(),
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    .await()
                Log.d("ProfileViewModel", "Firestore updated with avatarUrl: ${downloadUrl}")

                _uiState.value = _uiState.value.copy(
                    avatarUrl = downloadUrl.toString(),
                    isLoading = false
                )
                Log.d("ProfileViewModel", "UI state updated with new avatar URL")
                
                // Reload profile to ensure consistency
                loadUserProfile()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Avatar upload failed", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to upload avatar: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                accountService.signOut()
                _uiState.value = ProfileUiState()
                currentProfileId = null
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
        _uiState.value = ProfileUiState()
        currentProfileId = null
    }

    fun refreshProfile() {
        loadUserProfile()
    }
}
