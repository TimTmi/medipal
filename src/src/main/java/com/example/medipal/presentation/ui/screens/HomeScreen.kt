package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medipal.R
import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.viewmodel.CalendarUiState
import com.example.medipal.presentation.viewmodel.CalendarViewModel
import com.example.medipal.presentation.viewmodel.HomeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val events by viewModel.events.collectAsState(initial = emptyList())
    val isSheetVisible by viewModel.isAddSheetVisible.collectAsState()
    val calendarViewModel: CalendarViewModel = viewModel()

    // Bottom Sheet để chọn loại cần thêm
    if (isSheetVisible) {
        ModalBottomSheet(onDismissRequest = { viewModel.hideAddSheet() }) {
            AddOptionsSheet(
                onAddMedicine = {
                    viewModel.hideAddSheet()
                    navController.navigate(Screen.AddMedicineFlow.route)
                },
                onClose = { viewModel.hideAddSheet() }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.forest_background), // Đảm bảo bạn có ảnh này
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = { HomeScreenTopBar(onAddClick = { viewModel.showAddSheet() }) },
            containerColor = Color.Transparent
        ) { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Welcome, Tom",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    DynamicCalendarView(
                        viewModel = calendarViewModel
                    )
                }

                items(events) { event ->
                    EventCard(event = event)
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
            // Header giờ sẽ chuyển tuần thay vì tháng
            CalendarHeader(
                monthTitle = uiState.monthTitle,
                onPreviousClick = { viewModel.goToPreviousWeek() },
                onNextClick = { viewModel.goToNextWeek() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Logic chunked(7) vẫn hoạt động hoàn hảo cho 14 ngày -> 2 tuần
            val chunkedDates = uiState.dates.chunked(7)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                WeekDaysHeader()
                chunkedDates.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { date ->
                            DayCell(
                                date = date,
                                modifier = Modifier.weight(1f),
                                // Thêm sự kiện onClick
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
    // Giờ sẽ làm mờ ngày của tháng khác, chứ không chỉ là ngày không thuộc tháng hiện tại
    val textColor = if (date.date.monthValue == date.date.with(DayOfWeek.MONDAY).monthValue || date.isCurrentMonth) Color.White else Color.White.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            // Thêm sự kiện clickable
            .clickable(onClick = onClick)
            // Highlight ngày được chọn, không phải ngày hôm nay
            .background(if (date.isSelected) highlightColor else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.date.dayOfMonth.toString(),
            // Chữ màu đen trên nền highlight
            color = if(date.isSelected) Color.Black else textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(onAddClick: () -> Unit) {
    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun EventCard(event: ScheduledEvent) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val (title, type) = when (event) {
                is ScheduledEvent.Medication -> Pair(event.name, "Medication")
                is ScheduledEvent.Appointment -> Pair(event.title, "Appointment")
                is ScheduledEvent.Reminder -> Pair(event.title, "Reminder")
            }
            Text("${event.time} | $type\n$title", modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun AddOptionsSheet(onAddMedicine: () -> Unit, onClose: () -> Unit) {
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
            modifier = Modifier.clickable(onClick = { /* TODO */ })
        )
        ListItem(
            headlineContent = { Text("Add appointment") },
            leadingContent = { Icon(Icons.Default.Add, null) },
            modifier = Modifier.clickable(onClick = { /* TODO */ })
        )
    }
}