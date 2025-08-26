package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.service.AccountService
import com.example.medipal.domain.service.NotificationService
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthViewModel(
    private val accountService: AccountService,
    private val profileRepositoryManager: ProfileRepositoryManager,
    private val notificationService: NotificationService
) : ViewModel(), KoinComponent {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()
    
    init {
        // Monitor auth state changes continuously
        viewModelScope.launch {
            while (true) {
                try {
                    val account = accountService.getCurrentAccount()
                    val currentState = _authState.value
                    
                    if (account != null && currentState !is AuthState.Authenticated) {
                        profileRepositoryManager.setCurrentProfile(account.profileId)
                        _authState.value = AuthState.Authenticated(account)
                    } else if (account == null && currentState !is AuthState.Unauthenticated && currentState !is AuthState.Initial) {
                        profileRepositoryManager.setCurrentProfile("default-profile")
                        clearAllViewModelsData()
                        _authState.value = AuthState.Unauthenticated
                    }
                } catch (e: Exception) {
                    // Handle error silently
                }
                kotlinx.coroutines.delay(500) // Check every 500ms for faster logout detection
            }
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Form fields
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    
    // Profile fields for signup
    val fullName = MutableStateFlow("")
    val birthday = MutableStateFlow(0L)
    val height = MutableStateFlow(0f)
    val weight = MutableStateFlow(0f)
    val conditions = MutableStateFlow("")

    fun signIn() {
        if (email.value.isBlank() || password.value.isBlank()) {
            _errorMessage.value = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val account = accountService.signIn(email.value, password.value)
                if (account != null) {
                    // Set the current profile ID after successful login
                    profileRepositoryManager.setCurrentProfile(account.profileId)
                    _authState.value = AuthState.Authenticated(account)
                } else {
                    _errorMessage.value = "Invalid email or password"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Sign in failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUp() {
        if (email.value.isBlank() || password.value.isBlank() || fullName.value.isBlank()) {
            _errorMessage.value = "Please fill in all required fields"
            return
        }

        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Passwords do not match"
            return
        }

        if (password.value.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val profile = Profile(
                    fullName = fullName.value,
                    birthday = birthday.value,
                    height = height.value,
                    weight = weight.value,
                    conditions = conditions.value
                )
                
                val account = accountService.signUp(email.value, password.value, profile)
                // Set the current profile ID after successful signup
                profileRepositoryManager.setCurrentProfile(account.profileId)
                _authState.value = AuthState.Authenticated(account)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Sign up failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                accountService.signOut()
                // Reset to default profile when signing out
                profileRepositoryManager.setCurrentProfile("default-profile")
                
                // Clear data from all ViewModels that might have user-specific data
                clearAllViewModelsData()
                
                // Cancel all scheduled notifications for the old account
                clearAllScheduledNotifications()
                
                // Cancel all system notifications
                (notificationService as? com.example.medipal.data.service.NotificationServiceAndroidNotif)?.cancelAllNotifications()
                
                // Clear in-app notifications
                com.example.medipal.domain.service.InAppNotificationManager.clearAllNotifications()
                
                _authState.value = AuthState.Unauthenticated
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Sign out failed"
            }
        }
    }
    
    private fun clearAllViewModelsData() {
        try {
            // Clear HomeViewModel data by getting a fresh instance
            val homeViewModel: HomeViewModel? = getKoin().getOrNull()
            homeViewModel?.clearData()
            
            // Clear NotificationViewModel data
            val notificationViewModel: NotificationViewModel? = getKoin().getOrNull()
            notificationViewModel?.clearData()
            
            // Clear MedicationListViewModel data
            val medicationListViewModel: MedicationListViewModel? = getKoin().getOrNull()
            medicationListViewModel?.clearData()
            
            // Clear other ViewModels as needed
            val appointmentsViewModel: AppointmentsViewModel? = getKoin().getOrNull()
            appointmentsViewModel?.clearData()
            
            val remindersViewModel: RemindersViewModel? = getKoin().getOrNull()
            remindersViewModel?.clearData()
            
            val appointmentReminderViewModel: AppointmentReminderViewModel? = getKoin().getOrNull()
            appointmentReminderViewModel?.clearData()
            
            val historyViewModel: HistoryViewModel? = getKoin().getOrNull()
            historyViewModel?.clearData()
            
        } catch (e: Exception) {
            // Log error but don't fail logout
            println("Error clearing ViewModels data: ${e.message}")
        }
    }

    fun checkAuthState() {
        viewModelScope.launch {
            try {
                val account = accountService.getCurrentAccount()
                if (account != null) {
                    // Set the current profile ID when checking auth state
                    profileRepositoryManager.setCurrentProfile(account.profileId)
                    _authState.value = AuthState.Authenticated(account)
                } else {
                    // Reset to default profile when no account is found
                    profileRepositoryManager.setCurrentProfile("default-profile")
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                // Reset to default profile on error
                profileRepositoryManager.setCurrentProfile("default-profile")
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
    
    private fun clearAllScheduledNotifications() {
        viewModelScope.launch {
            try {
                // Get all current data before clearing to cancel their notifications
                val homeViewModel: HomeViewModel? = getKoin().getOrNull()
                val notificationViewModel: NotificationViewModel? = getKoin().getOrNull()
                
                // Cancel all scheduled notifications by clearing the notification service
                // Since we don't have direct access to current data flows, we'll rely on
                // the notification service's cancelAllNotifications method
                (notificationService as? com.example.medipal.data.service.NotificationServiceAndroidNotif)?.cancelAllNotifications()
            } catch (e: Exception) {
                println("Error clearing scheduled notifications: ${e.message}")
            }
        }
    }

    private fun clearForm() {
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
        fullName.value = ""
        birthday.value = 0L
        height.value = 0f
        weight.value = 0f
        conditions.value = ""
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val account: Account) : AuthState()
}
