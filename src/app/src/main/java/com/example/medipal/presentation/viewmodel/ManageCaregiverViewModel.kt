package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.AccountType
import com.example.medipal.domain.model.CaregiverAssignment
import com.example.medipal.domain.repository.CaregiverAssignmentRepository
import com.example.medipal.domain.service.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ManageCaregiverViewModel(
    private val caregiverAssignmentRepository: CaregiverAssignmentRepository,
    private val accountService: AccountService
) : ViewModel() {

    private val _assignments = MutableStateFlow<List<CaregiverAssignment>>(emptyList())
    val assignments = _assignments.asStateFlow()

    private val _linkedAccountsWithNames = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val linkedAccountsWithNames = _linkedAccountsWithNames.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    lateinit var account: Account
        private set

    fun initialize(currentAccount: Account) {
        account = currentAccount
        loadAssignments()
    }

    private fun loadAssignments() {
        viewModelScope.launch {
            try {
                val flow = if (account.type == AccountType.CUSTOMER) {
                    caregiverAssignmentRepository.getAssignmentsForCustomer(account.id)
                } else {
                    caregiverAssignmentRepository.getAssignmentsForCaregiver(account.id)
                }
                
                flow.collect { assignmentList ->
                    _assignments.value = assignmentList
                    loadAccountNamesForAssignments(assignmentList)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load assignments"
            }
        }
    }

    private suspend fun loadAccountNamesForAssignments(assignmentList: List<CaregiverAssignment>) {
        val accountsWithNames = coroutineScope {
            assignmentList.map { assignment ->
                async {
                    val targetAccountId = if (account.type == AccountType.CUSTOMER) {
                        assignment.caregiverId
                    } else {
                        assignment.customerId
                    }
                    
                    val targetAccount = accountService.getAccount(targetAccountId)
                    val profileName = targetAccount?.profileId?.let { profileId ->
                        accountService.getProfile(profileId)?.fullName
                    } ?: "Unknown"
                    
                    targetAccountId to profileName
                }
            }.map { it.await() }
        }
        
        _linkedAccountsWithNames.value = accountsWithNames
    }

    fun addLink(targetAccountId: String) {
        viewModelScope.launch {
            try {
                // Verify the target account exists
                val targetAccount = accountService.getAccount(targetAccountId)
                if (targetAccount == null) {
                    _errorMessage.value = "Account not found"
                    return@launch
                }

                val assignment = if (account.type == AccountType.CUSTOMER) {
                    // Customer adding a caregiver
                    if (targetAccount.type != AccountType.CAREGIVER) {
                        _errorMessage.value = "Target account must be a caregiver"
                        return@launch
                    }
                    CaregiverAssignment(
                        caregiverId = targetAccountId,
                        customerId = account.id
                    )
                } else {
                    // Caregiver adding a customer
                    if (targetAccount.type != AccountType.CUSTOMER) {
                        _errorMessage.value = "Target account must be a customer"
                        return@launch
                    }
                    CaregiverAssignment(
                        caregiverId = account.id,
                        customerId = targetAccountId
                    )
                }

                caregiverAssignmentRepository.addAssignment(assignment)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to add link"
            }
        }
    }

    fun removeLink(targetAccountId: String) {
        viewModelScope.launch {
            try {
                val (caregiverId, customerId) = if (account.type == AccountType.CUSTOMER) {
                    targetAccountId to account.id
                } else {
                    account.id to targetAccountId
                }

                caregiverAssignmentRepository.removeAssignment(caregiverId, customerId)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to remove link"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
