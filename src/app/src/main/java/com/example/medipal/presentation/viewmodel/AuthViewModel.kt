package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.AccountType
import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.service.AccountService
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val accountService: AccountService,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()

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
    val accountType = MutableStateFlow(AccountType.CUSTOMER)
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
                    profileRepositoryManager.setCurrentProfile(account.profileId)
//                    // Set the current profile ID after successful login
//                    if (account.type == AccountType.CUSTOMER) {
//                        profileRepositoryManager.setCurrentProfile(account.profileId)
//                    } else {
//                        // Caregivers don't have profiles, set default
//                        profileRepositoryManager.setCurrentProfile("caregiver-mode")
//                    }
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
                
                val account = accountService.signUp(email.value, password.value, profile, accountType.value)
                profileRepositoryManager.setCurrentProfile(account.profileId)
//                // Set the current profile ID after successful signup
//                if (account.type == AccountType.CUSTOMER) {
//                    profileRepositoryManager.setCurrentProfile(account.profileId)
//                } else {
//                    // Caregivers don't have profiles, set default
//                    profileRepositoryManager.setCurrentProfile("caregiver-mode")
//                }
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
                _authState.value = AuthState.Unauthenticated
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Sign out failed"
            }
        }
    }

    fun checkAuthState() {
        viewModelScope.launch {
            try {
                val account = accountService.getCurrentAccount()
                if (account != null) {
                    profileRepositoryManager.setCurrentProfile(account.profileId)
//                    // Set the current profile ID when checking auth state
//                    if (account.type == AccountType.CUSTOMER) {
//                        profileRepositoryManager.setCurrentProfile(account.profileId)
//                    } else {
//                        // Caregivers don't have profiles, set default
//                        profileRepositoryManager.setCurrentProfile("caregiver-mode")
//                    }
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
