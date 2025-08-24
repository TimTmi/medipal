package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medipal.presentation.viewmodel.MedicationDetailViewModel // Quan trọng: Dùng đúng ViewModel
import com.example.medipal.presentation.ui.components.FrequencyOptionRow
import com.example.medipal.presentation.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFrequencyScreen(
    navController: NavController,
    viewModel: MedicationDetailViewModel // Sử dụng MedicationDetailViewModel
) {
    val options = viewModel.baseFrequencyOptions
    val selectedFrequencyObject by viewModel.selectedFrequencyObject.collectAsState()
    val medicineName by viewModel.medicineName.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(medicineName) },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "How often do you take it?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(options) { option ->
                    FrequencyOptionRow(
                        text = option,
                        selected = (option == selectedFrequencyObject.displayText),
                        onClick = {
                            when (option) {
                                "Every day" -> viewModel.setFrequencyEveryDay()
                                "Only as needed" -> viewModel.setFrequencyAsNeeded()
                                "Every X days" -> {
                                    // Navigate to X days screen
                                    navController.navigate(Screen.EditMedicineXDays.route)
                                }
                                "Specific days of the week" -> {
                                    // Navigate to specific days screen
                                    navController.navigate(Screen.EditMedicineSpecificDays.route)
                                }
                                "Every X weeks" -> {
                                    // Navigate to X weeks screen
                                    navController.navigate(Screen.EditMedicineXWeeks.route)
                                }
                            }
                            if (option == "Every day" || option == "Only as needed") {
                                navController.popBackStack()
                            }
                        }
                    )
                }
            }
        }
    }
}

