package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
import com.example.medipal.presentation.ui.components.TimePickerDialog
import androidx.compose.foundation.clickable
import java.util.Locale

// Các route cho các bước con bên trong luồng thêm healthcare reminder
private const val STEP_TITLE = "step_title"
private const val STEP_TIME = "step_time"

@Composable
fun AddHealthcareReminderFlow(
    mainNavController: NavController,
    viewModel: AddHealthcareReminderViewModel
) {
    val flowNavController = rememberNavController()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val lastSavedReminder by viewModel.lastSavedReminderTitle.collectAsState()

    if (showSuccessDialog && lastSavedReminder != null) {
        ReminderSuccessDialog(
            reminderTitle = lastSavedReminder!!,
            onDismiss = {
                viewModel.dismissSuccessDialog()
                mainNavController.popBackStack()
            }
        )
    }

    NavHost(navController = flowNavController, startDestination = STEP_TITLE) {
        composable(STEP_TITLE) {
            SelectReminderTitleScreen(
                viewModel = viewModel,
                onNext = { flowNavController.navigate(STEP_TIME) },
                onCancel = { mainNavController.popBackStack() }
            )
        }
        composable(STEP_TIME) {
            SelectReminderTimeScreen(
                viewModel = viewModel,
                onSave = { viewModel.saveHealthcareReminder() },
                onCancel = { mainNavController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectReminderTitleScreen(
    viewModel: AddHealthcareReminderViewModel, 
    onNext: () -> Unit, 
    onCancel: () -> Unit
) {
    val reminderTitle by viewModel.reminderTitle.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add healthcare reminder") },
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
            Text("What healthcare reminder would you like to add?")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = reminderTitle,
                onValueChange = { viewModel.reminderTitle.value = it },
                label = { Text("Reminder Title") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g., Drink water, Take a walk, Check blood pressure") }
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
fun SelectReminderTimeScreen(
    viewModel: AddHealthcareReminderViewModel, 
    onSave: () -> Unit, 
    onCancel: () -> Unit
) {
    val reminderTitle by viewModel.reminderTitle.collectAsState()
    val time by viewModel.time.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(reminderTitle) },
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
            Text(
                "When do you want to be reminded?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Time Display Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { showTimePicker = true },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Selected Time",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = time,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to change",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Save Reminder")
            }
        }
    }
    
    // Time Picker Dialog
    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { hour, minute ->
                val amPm = if (hour >= 12) "PM" else "AM"
                val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val timeString = String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minute, amPm)
                viewModel.updateTime(timeString)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
fun ReminderSuccessDialog(
    reminderTitle: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Successfully Added") },
        text = { Text(text = "$reminderTitle has been added to your reminders.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
} 