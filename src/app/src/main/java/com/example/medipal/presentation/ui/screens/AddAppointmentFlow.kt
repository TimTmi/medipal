package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medipal.presentation.viewmodel.AddAppointmentViewModel
import com.example.medipal.presentation.ui.components.TimePickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek
import java.util.*
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentFlow(
    mainNavController: NavController,
    viewModel: AddAppointmentViewModel
) {
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val lastSavedAppointment by viewModel.lastSavedAppointmentTitle.collectAsState()

    if (showSuccessDialog && lastSavedAppointment != null) {
        AppointmentSuccessDialog(
            appointmentTitle = lastSavedAppointment!!,
            onDismiss = {
                viewModel.dismissSuccessDialog()
                mainNavController.popBackStack()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointment details") },
                navigationIcon = { 
                    TextButton(onClick = { mainNavController.popBackStack() }) { 
                        Text("Cancel") 
                    } 
                }
            )
        }
    ) { padding ->
        AppointmentFormScreen(
            viewModel = viewModel,
            onSave = { viewModel.saveAppointment() },
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentFormScreen(
    viewModel: AddAppointmentViewModel,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val doctorName by viewModel.doctorName.collectAsState()
    val location by viewModel.location.collectAsState()
    val date by viewModel.date.collectAsState()
    val time by viewModel.time.collectAsState()
    val reasonForVisit by viewModel.reasonForVisit.collectAsState()
    
    val doctorNameError by viewModel.doctorNameError.collectAsState()
    val locationError by viewModel.locationError.collectAsState()
    val dateError by viewModel.dateError.collectAsState()
    val timeError by viewModel.timeError.collectAsState()
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Doctor's name field
        OutlinedTextField(
            value = doctorName,
            onValueChange = { 
                viewModel.doctorName.value = it
                if (doctorNameError != null) {
                    viewModel.clearDoctorNameError()
                }
            },
            label = { Text("Doctor's name") },
            placeholder = { Text("Input doctor's name") },
            modifier = Modifier.fillMaxWidth(),
            isError = doctorNameError != null,
            supportingText = doctorNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        // Location field
        OutlinedTextField(
            value = location,
            onValueChange = { 
                viewModel.location.value = it
                if (locationError != null) {
                    viewModel.clearLocationError()
                }
            },
            label = { Text("Location") },
            placeholder = { Text("Input location") },
            modifier = Modifier.fillMaxWidth(),
            isError = locationError != null,
            supportingText = locationError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        // Date field
        OutlinedTextField(
            value = date,
            onValueChange = { viewModel.date.value = it },
            label = { Text("Date") },
            placeholder = { Text("Input date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = dateError != null,
            supportingText = dateError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                TextButton(onClick = { 
                    showDatePicker = true
                    if (dateError != null) {
                        viewModel.clearDateError()
                    }
                }) {
                    Text("Select")
                }
            }
        )

        // Time field
        OutlinedTextField(
            value = time,
            onValueChange = { viewModel.time.value = it },
            label = { Text("Time") },
            placeholder = { Text("Input time") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = timeError != null,
            supportingText = timeError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                TextButton(onClick = { 
                    showTimePicker = true
                    if (timeError != null) {
                        viewModel.clearTimeError()
                    }
                }) {
                    Text("Select")
                }
            }
        )

        // Reason for visit field
        OutlinedTextField(
            value = reasonForVisit,
            onValueChange = { viewModel.reasonForVisit.value = it },
            label = { Text("Reason for visit") },
            placeholder = { Text("Input reason") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.weight(1f))

        // Save button
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = doctorName.isNotBlank() && location.isNotBlank() && date.isNotBlank() && time.isNotBlank()
        ) {
            Text("OK", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        CalendarDatePickerDialog(
            onDateSelected = { selectedDate ->
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                viewModel.date.value = selectedDate.format(formatter)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // Time Picker Dialog
    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { hour, minute ->
                val amPm = if (hour >= 12) "PM" else "AM"
                val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val timeString = String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minute, amPm)
                viewModel.time.value = timeString
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
fun CalendarDatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(LocalDate.now()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { 
                        currentMonth = currentMonth.minusMonths(1)
                    }
                ) {
                    Text("‹", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(
                    onClick = { 
                        currentMonth = currentMonth.plusMonths(1)
                    }
                ) {
                    Text("›", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Week days header
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Calendar grid
                val firstDayOfMonth = currentMonth.withDayOfMonth(1)
                val lastDayOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth())
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
                val startDate = firstDayOfMonth.minusDays((firstDayOfWeek - 1).toLong())
                
                val calendarDays = (0..41).map { dayOffset ->
                    startDate.plusDays(dayOffset.toLong())
                }

                // Calendar rows
                calendarDays.chunked(7).forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { date ->
                            val isCurrentMonth = date.monthValue == currentMonth.monthValue
                            val isSelected = date.isEqual(selectedDate)
                            val isToday = date.isEqual(LocalDate.now())
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday -> MaterialTheme.colorScheme.primaryContainer
                                            else -> MaterialTheme.colorScheme.surface
                                        },
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                                    .clickable { selectedDate = date },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                                        isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    },
                                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text("OK", fontWeight = FontWeight.Bold)
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
fun AppointmentSuccessDialog(
    appointmentTitle: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Successfully Added") },
        text = { Text(text = "$appointmentTitle has been added to your schedule.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
} 