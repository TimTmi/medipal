package com.example.medipal.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.AccountType
import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.repository.CaregiverAssignmentRepository
import com.example.medipal.util.ProfileRepositoryManager
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.medipal.domain.service.AccountService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.compose.koinInject


@Composable
fun TopBar(
    account: Account?,
    caregiverAssignmentRepository: CaregiverAssignmentRepository,
    profileRepositoryManager: ProfileRepositoryManager
) {
    TopAppBar(
        title = {
            Text("MediPal")
        }
    )
}
