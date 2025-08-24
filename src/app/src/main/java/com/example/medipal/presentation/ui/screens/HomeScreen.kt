package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medipal.R
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.viewmodel.CalendarUiState
import com.example.medipal.presentation.viewmodel.CalendarViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val medications by viewModel.medications.collectAsState(initial = emptyList())
    val appointments by viewModel.appointments.collectAsState(initial = emptyList())
    val reminders by viewModel.reminders.collectAsState(initial = emptyList())
    val isSheetVisible by viewModel.isAddSheetVisible.collectAsState()
    val calendarViewModel: CalendarViewModel = viewModel()

    if (isSheetVisible) {
        ModalBottomSheet(onDismissRequest = { viewModel.hideAddSheet() }) {
            AddOptionsSheet(
                onAddMedicine = {
                    viewModel.hideAddSheet()
                    navController.navigate(Screen.AddMedicineFlow.route)
                },
                onAddHealthcareReminder = {
                    viewModel.hideAddSheet()
                    navController.navigate(Screen.AddHealthcareReminderFlow.route)
                },
                onAddAppointment = {
                    viewModel.hideAddSheet()
                    navController.navigate(Screen.AddAppointmentFlow.route)
                },
                onClose = { viewModel.hideAddSheet() }
            )
        }
    }
    val brightness = 0.5f
    val colorMatrix = ColorMatrix().apply {
        setToScale(brightness, brightness, brightness, 1f)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.forest_background),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.90f),
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )

        // THAY ĐỔI QUAN TRỌNG: Lớp phủ (scrim) đã được XÓA BỎ
        // Vì giờ chúng ta đã có icon tối màu nên không cần làm tối nền nữa.

        Scaffold(
            topBar = { 
                HomeScreenTopBar(
                    onAddClick = { viewModel.showAddSheet() },
                    onHistoryClick = { navController.navigate(Screen.HistoryLog.route) }
                ) 
            },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Welcome, bro",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    DynamicCalendarView(
                        viewModel = calendarViewModel
                    )
                }

                items(medications) { medication ->
                    MedicationCard(medication = medication)
                }
                
                items(appointments) { appointment ->
                    AppointmentCard(appointment = appointment)
                }
                
                items(reminders) { reminder ->
                    ReminderCard(reminder = reminder)
                }
            }
        }
    }
}
@Composable
fun DynamicCalendarView(viewModel: CalendarViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val darkGreenBackground = Color(0xFF1C5F55)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = darkGreenBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CalendarHeader(
                monthTitle = uiState.monthTitle,
                onPreviousClick = { viewModel.goToPreviousWeek() },
                onNextClick = { viewModel.goToNextWeek() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            val chunkedDates = uiState.dates.chunked(7)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                WeekDaysHeader()
                chunkedDates.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { date ->
                            DayCell(
                                date = date,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onDateSelected(date.date) }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CalendarHeader(monthTitle: String, onPreviousClick: () -> Unit, onNextClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = monthTitle,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Row {
            IconButton(onClick = onPreviousClick, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.ChevronLeft, "Previous", tint = Color.White)
            }
            IconButton(onClick = onNextClick, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.ChevronRight, "Next", tint = Color.White)
            }
        }
    }
}

@Composable
fun WeekDaysHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
            Text(
                text = day,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DayCell(
    date: CalendarUiState.Date,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val highlightColor = Color(0xFFA7BEB5)
    val textColor = if (date.date.monthValue == date.date.with(DayOfWeek.MONDAY).monthValue || date.isCurrentMonth) Color.White else Color.White.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(if (date.isSelected) highlightColor else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.date.dayOfMonth.toString(),
            color = if(date.isSelected) Color.Black else textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    onAddClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    TopAppBar(
        title = { },
        windowInsets = WindowInsets.statusBars,
        actions = {
            IconButton(onClick = onHistoryClick) {
                Icon(
                    Icons.Default.History, 
                    contentDescription = "History", 
                    tint = Color.White, 
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun MedicationCard(medication: Medication) {
    val formattedTime = Instant.ofEpochMilli(medication.scheduleTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("hh:mm a"))
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$formattedTime | Medication",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = medication.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Frequency: ${medication.frequency?.displayText ?: medication.frequency?.toString() ?: "Every day"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, onClick: () -> Unit = {}) {
    val formattedTime = Instant.ofEpochMilli(appointment.dateTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("hh:mm a"))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$formattedTime | Appointment",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = appointment.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Doctor: ${appointment.doctorName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun ReminderCard(reminder: Reminder) {
    val formattedTime = Instant.ofEpochMilli(reminder.dateTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("hh:mm a"))
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$formattedTime | Reminder",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun AddOptionsSheet(
    onAddMedicine: () -> Unit, 
    onAddHealthcareReminder: () -> Unit,
    onAddAppointment: () -> Unit,
    onClose: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("What would you like to add?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) { Icon(Icons.Default.Close, "Close") }
        }
        Spacer(Modifier.height(16.dp))
        ListItem(
            headlineContent = { Text("Add medicine") },
            leadingContent = { Icon(Icons.Default.Add, null) },
            modifier = Modifier.clickable(onClick = onAddMedicine)
        )
        ListItem(
            headlineContent = { Text("Add healthcare reminder") },
            leadingContent = { Icon(Icons.Default.Add, null) },
            modifier = Modifier.clickable(onClick = onAddHealthcareReminder)
        )
        ListItem(
            headlineContent = { Text("Add appointment") },
            leadingContent = { Icon(Icons.Default.Add, null) },
            modifier = Modifier.clickable(onClick = onAddAppointment)
        )
    }
}