import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.CaregiverAssignment
import com.example.medipal.domain.repository.CaregiverAssignmentRepository
import com.example.medipal.domain.service.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//class ManageCaregiverViewModel(
//    private val caregiverAssignmentRepository: CaregiverAssignmentRepository,
//    private val accountService: AccountService
//) : ViewModel() {
//
//    // Holds the current logged-in account
//    private val _account = MutableStateFlow<Account?>(null)
//    val account: StateFlow<Account?> get() = _account
//
//    // Holds current assignments
//    private val _assignments = MutableStateFlow<List<CaregiverAssignment>>(emptyList())
//    val assignments: StateFlow<List<CaregiverAssignment>> get() = _assignments
//
//    init {
//        loadAccountAndAssignments()
//    }
//
//    private fun loadAccountAndAssignments() {
//        viewModelScope.launch {
//            val currentAccount = accountService.getCurrentAccount()
//            if (currentAccount != null) {
//                _account.value = currentAccount
//                caregiverAssignmentRepository
//                    .getAssignmentsForCaregiver(currentAccount.id)
//                    .collect { list -> _assignments.value = list }
//            }
//
//        }
//    }
//
//    fun addLink(newId: String) {
//        viewModelScope.launch {
//            val currentAccount = _account.value ?: return@launch
//            caregiverAssignmentRepository.addAssignment(CaregiverAssignment(currentAccount.id, newId))
//        }
//    }
//
//    fun removeLink(linkedId: String) {
//        viewModelScope.launch {
//            val currentAccount = _account.value ?: return@launch
//            caregiverAssignmentRepository.removeAssignment(currentAccount.id, linkedId)
//        }
//    }
//}
