package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.medipal.presentation.viewmodel.AddMedicationViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import com.example.medipal.presentation.ui.components.TimePickerDialog
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.Locale

// Các route cho các bước con bên trong luồng thêm thuốc
private const val STEP_NAME = "step_name"
private const val STEP_FREQUENCY = "step_frequency"
private const val STEP_TIME = "step_time"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineFlow(
    mainNavController: NavController,
    viewModel: AddMedicationViewModel
) {
    val flowNavController = rememberNavController()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val lastSavedMedicine by viewModel.lastSavedMedicineName.collectAsState()

    // 2. Chỉ hiển thị dialog khi cần thiết và có tên thuốc để hiển thị
    if (showSuccessDialog && lastSavedMedicine != null) {
        SuccessDialog(
            medicineName = lastSavedMedicine!!, // Truyền tên thuốc vào
            onDismiss = {
                viewModel.dismissSuccessDialog()
                mainNavController.popBackStack()
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
fun SelectMedicineNameScreen(viewModel: AddMedicationViewModel, onNext: () -> Unit, onCancel: () -> Unit) {
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
fun SelectFrequencyScreen(viewModel: AddMedicationViewModel, onNext: () -> Unit, onCancel: () -> Unit) {
    val medicineName by viewModel.medicineName.collectAsState()

    // SỬA LỖI Ở ĐÂY: Lấy danh sách tĩnh trực tiếp, không dùng collectAsState
    val options = viewModel.frequencyOptions

    // Dòng này đúng vì selectedFrequency là StateFlow
    val selectedOption by viewModel.selectedFrequency.collectAsState()

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeScreen(viewModel: AddMedicationViewModel, onSave: () -> Unit, onCancel: () -> Unit) {
    val medicineName by viewModel.medicineName.collectAsState()
    val time by viewModel.time.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }

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
            val formattedTime = Instant.ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern("hh:mm a"))

            Text(
                "When do you need to take it?",
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
                        text = formattedTime,
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
                Text("Save Medicine")
            }
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { hour, minute ->
                val now = LocalDate.now()
                val selectedTime = LocalDateTime.of(now, LocalTime.of(hour, minute))
                val epochMillis = selectedTime
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                viewModel.updateTime(epochMillis)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessDialog(
    medicineName: String, // Thêm tham số medicineName
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Successfully Added") },
        // Sử dụng medicineName trong nội dung text
        text = { Text(text = "$medicineName has been added to your schedule.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}