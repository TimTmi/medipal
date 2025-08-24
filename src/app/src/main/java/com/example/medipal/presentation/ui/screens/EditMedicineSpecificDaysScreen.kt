package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medipal.presentation.viewmodel.MedicationDetailViewModel
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicineSpecificDaysScreen(
    navController: NavController,
    viewModel: MedicationDetailViewModel
) {
    val medicineName by viewModel.medicineName.collectAsState()
    val selectedWeekDays by viewModel.selectedWeekDays.collectAsState()
    
    var localSelectedDays by remember { mutableStateOf(selectedWeekDays) }

    val weekDays = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

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
                "Select specific days of the week",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            LazyColumn {
                items(weekDays) { day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = localSelectedDays.contains(day),
                            onCheckedChange = { checked ->
                                localSelectedDays = if (checked) {
                                    localSelectedDays + day
                                } else {
                                    localSelectedDays - day
                                }
                            }
                        )
                        Text(
                            text = day.name.lowercase().replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    viewModel.saveFrequencySpecificDays(localSelectedDays)
                    // Navigate back to EditMedicine screen
                    navController.popBackStack(
                        route = "edit_medicine/{medicationId}",
                        inclusive = false
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = localSelectedDays.isNotEmpty()
            ) {
                Text("Save")
            }
        }
    }
}
