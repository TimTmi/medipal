package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medipal.presentation.viewmodel.AddMedicineViewModel

// Các route cho các bước con bên trong luồng thêm thuốc
private const val STEP_NAME = "step_name"
private const val STEP_FREQUENCY = "step_frequency"
private const val STEP_TIME = "step_time"

@Composable
fun AddMedicineFlow(
    mainNavController: NavController,
    viewModel: AddMedicineViewModel
) {
    val flowNavController = rememberNavController()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()

    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                viewModel.dismissSuccessDialog()
                mainNavController.popBackStack() // Quay về màn hình Home
            }
        )
    }

    NavHost(navController = flowNavController, startDestination = STEP_NAME) {
        composable(STEP_NAME) {
            SelectMedicineNameScreen(
                viewModel = viewModel,
                onNext = { flowNavController.navigate(STEP_FREQUENCY) },
                onCancel = { mainNavController.popBackStack() }
            )
        }
        composable(STEP_FREQUENCY) {
            SelectFrequencyScreen(
                viewModel = viewModel,
                onNext = { flowNavController.navigate(STEP_TIME) },
                onCancel = { mainNavController.popBackStack() }
            )
        }
        composable(STEP_TIME) {
            SelectTimeScreen(
                viewModel = viewModel,
                onSave = { viewModel.saveMedication() }, // Lưu và hiển thị dialog
                onCancel = { mainNavController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMedicineNameScreen(viewModel: AddMedicineViewModel, onNext: () -> Unit, onCancel: () -> Unit) {
    val medicineName by viewModel.medicineName.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add medicine") },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
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
            Text("What medicine would you like to add?")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = medicineName,
                onValueChange = { viewModel.medicineName.value = it },
                label = { Text("Medicine Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFrequencyScreen(viewModel: AddMedicineViewModel, onNext: () -> Unit, onCancel: () -> Unit) {
    val medicineName by viewModel.medicineName.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(medicineName) },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
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
            Text("How often do you take it?")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Every day", modifier = Modifier.padding(16.dp))
            Text("Only as needed", modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeScreen(viewModel: AddMedicineViewModel, onSave: () -> Unit, onCancel: () -> Unit) {
    val medicineName by viewModel.medicineName.collectAsState()
    val time by viewModel.time.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(medicineName) },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
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
            Text("When do you need to take it?")
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = time,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onSave) {
                Text("Save")
            }
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Successfully Added") },
        text = { Text(text = "Paracetamol has been added to your schedule.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}