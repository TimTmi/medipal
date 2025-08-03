package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.CenterAlignedTopAppBar
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel
import com.example.medipal.presentation.ui.components.TimePickerDialog
import java.util.Locale

// Các route cho các bước con
private const val STEP_CATEGORY = "reminder_category"
private const val STEP_ACTIVITY = "reminder_activity"
private const val STEP_FREQUENCY_REMINDER = "reminder_frequency"
private const val STEP_TIME = "reminder_time"

@Composable
fun AddHealthcareReminderFlow(
    mainNavController: NavController,
    viewModel: AddHealthcareReminderViewModel
) {
    val flowNavController = rememberNavController()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val lastSavedReminderTitle by viewModel.lastSavedReminderTitle.collectAsState()

    if (showSuccessDialog && lastSavedReminderTitle != null) {
        SuccessDialog(
            title = "Successfully Added",
            message = "$lastSavedReminderTitle Reminder",
            onDismiss = {
                viewModel.dismissSuccessDialog()
                mainNavController.popBackStack()
            }
        )
    }

    NavHost(navController = flowNavController, startDestination = STEP_CATEGORY) {
        composable(STEP_CATEGORY) {
            ReminderCategoryScreen(
                categories = viewModel.reminderCategories.keys.toList(),
                onCategoryClick = { category ->
                    viewModel.onCategorySelected(category)
                    flowNavController.navigate(STEP_ACTIVITY)
                },
                onCancel = { mainNavController.popBackStack() }
            )
        }
        composable(STEP_ACTIVITY) {
            ReminderActivityScreen(
                viewModel = viewModel,
                onNext = { flowNavController.navigate(STEP_FREQUENCY_REMINDER) },
                onCancel = { mainNavController.popBackStack() }
            )
        }
        composable(STEP_FREQUENCY_REMINDER) {
            ReminderFrequencyScreen(
                viewModel = viewModel,
                onNext = { flowNavController.navigate(STEP_TIME) },
                onCancel = { mainNavController.popBackStack() }
            )
        }
        composable(STEP_TIME) {
            val selectedActivity by viewModel.selectedActivity.collectAsState()
            ReminderTimeScreen(
                viewModel = viewModel,
                onSave = { viewModel.saveReminder() },
                onCancel = { mainNavController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderCategoryScreen(
    categories: List<String>,
    onCategoryClick: (String) -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add healthcare reminder") },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding, modifier = Modifier.padding(16.dp)) {
            items(categories) { category ->
                ListItem(
                    headlineContent = { Text(category) },
                    modifier = Modifier.clickable { onCategoryClick(category) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderActivityScreen(
    viewModel: AddHealthcareReminderViewModel,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val activities = viewModel.reminderCategories[selectedCategory] ?: emptyList()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Setup $selectedCategory Reminder") },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select an activity",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedActivity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select activity") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    activities.forEach { activity ->
                        DropdownMenuItem(
                            text = { Text(activity) },
                            onClick = {
                                viewModel.onActivitySelected(activity)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderFrequencyScreen(
    viewModel: AddHealthcareReminderViewModel,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val options = viewModel.frequencyOptions
    val selectedOption by viewModel.selectedFrequency.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(selectedActivity) },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
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
                "How often do you do it?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(options) { option ->
                    ReminderFrequencyOptionRow(
                        text = option,
                        selected = (option == selectedOption),
                        onClick = { viewModel.onFrequencySelected(option) }
                    )
                }
            }

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun ReminderFrequencyOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text) },
        leadingContent = {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimeScreen(
    viewModel: AddHealthcareReminderViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()
    val sessionCount by viewModel.sessionCount.collectAsState()
    var showSessionDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showSessionDialog) {
        EditSessionDialog(
            currentCount = sessionCount,
            onDismiss = { showSessionDialog = false },
            onConfirm = { newCount ->
                viewModel.onSessionCountChanged(newCount)
                showSessionDialog = false
            }
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { hour, minute ->
                val amPm = if (hour >= 12) "PM" else "AM"
                val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val timeString = String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minute, amPm)
                viewModel.onTimeSelected(timeString)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(selectedActivity) },
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
                "What time should you do it?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Session Count Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Do")
                    Text("$sessionCount session(s)")
                    IconButton(onClick = { showSessionDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit session count")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Display Card (copy style from AddMedicine)
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
                        text = selectedTime,
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
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("Save")
            }
        }
    }
}

@Composable
fun EditSessionDialog(
    currentCount: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var textValue by remember { mutableStateOf(currentCount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Session Count") },
        text = {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it.filter { char -> char.isDigit() } },
                label = { Text("Number of sessions") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newCount = textValue.toIntOrNull() ?: 1
                    onConfirm(newCount)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SuccessDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } }
    )
} 