package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medipal.presentation.viewmodel.MedicationDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicineXDaysScreen(
    navController: NavController,
    viewModel: MedicationDetailViewModel
) {
    val medicineName by viewModel.medicineName.collectAsState()
    val xDaysValue by viewModel.xDaysValue.collectAsState()
    
    var localXDaysValue by remember { mutableStateOf(xDaysValue) }

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Every how many days?",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            // Number input for days
            OutlinedTextField(
                value = localXDaysValue.toString(),
                onValueChange = { 
                    localXDaysValue = it.toIntOrNull() ?: 1
                },
                label = { Text("Number of days") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
                modifier = Modifier.width(200.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    viewModel.saveFrequencyXDays(localXDaysValue)
                    // Navigate back to EditMedicine screen
                    navController.popBackStack(
                        route = "edit_medicine/{medicationId}",
                        inclusive = false
                    )
                },
                modifier = Modifier.width(200.dp)
            ) {
                Text("Save")
            }
        }
    }
}
