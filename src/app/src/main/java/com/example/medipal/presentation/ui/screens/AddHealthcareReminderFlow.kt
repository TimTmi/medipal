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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.CenterAlignedTopAppBar
import com.example.medipal.presentation.viewmodel.AddHealthcareReminderViewModel

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
            // SỬA Ở ĐÂY: Dùng CenterAlignedTopAppBar
            CenterAlignedTopAppBar(
                title = { Text("Add healthcare reminder") },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
                // Không cần action Spacer nữa
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

// Màn hình 2: Chọn hoạt động
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
            // SỬA Ở ĐÂY: Dùng CenterAlignedTopAppBar
            CenterAlignedTopAppBar(
                title = { Text("Setup $selectedCategory Reminder") },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel") } }
                // Không cần action Spacer nữa
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // ... nội dung không đổi ...
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
    // 1. Lấy trạng thái từ ViewModel
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val options = viewModel.frequencyOptions
    val selectedOption by viewModel.selectedFrequency.collectAsState()

    Scaffold(
        topBar = {
            // 2. Dùng CenterAlignedTopAppBar để căn giữa hoàn hảo
            CenterAlignedTopAppBar(
                title = { Text(selectedActivity) }, // Hiển thị tên hoạt động làm tiêu đề
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
            // 3. Tiêu đề của màn hình
            Text(
                "How often do you do it?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Hiển thị danh sách lựa chọn bằng LazyColumn
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(options) { option ->
                    // 5. Tái sử dụng FrequencyOptionRow
                    FrequencyOptionRow(
                        text = option,
                        selected = (option == selectedOption),
                        // Gọi hàm trong ViewModel khi có sự kiện click
                        onClick = { viewModel.onFrequencySelected(option) }
                    )
                }
            }

            // 6. Nút điều hướng
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}


// Màn hình 4: Chọn thời gian
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimeScreen(
    viewModel: AddHealthcareReminderViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val sessionCount by viewModel.sessionCount.collectAsState()

    // 1. Thêm trạng thái để điều khiển việc hiển thị dialog
    var showSessionDialog by remember { mutableStateOf(false) }

    // 2. Hiển thị dialog khi trạng thái là true
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
            Text("What time should you do it?")
            Spacer(modifier = Modifier.height(16.dp))

            // --- PHẦN MỚI: CHỌN SỐ PHIÊN ---
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
            // --- KẾT THÚC PHẦN MỚI ---

            Spacer(modifier = Modifier.height(16.dp))

            // Giả lập TimePicker
            Text(text = "16 : 46 AM", style = MaterialTheme.typography.displayMedium)

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
    // Trạng thái cho TextField bên trong dialog
    var textValue by remember { mutableStateOf(currentCount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Session Count") },
        text = {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it.filter { char -> char.isDigit() } }, // Chỉ cho phép nhập số
                label = { Text("Number of sessions") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Chuyển đổi text thành số và gọi onConfirm
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



// Dialog thành công chung (có thể giữ ở đây hoặc chuyển ra file riêng)
@Composable
fun SuccessDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } }
    )
}