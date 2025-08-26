package com.example.medipal.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medipal.presentation.viewmodel.CalendarUiState
import com.example.medipal.presentation.viewmodel.CalendarViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                monthTitle = uiState.selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
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
    val textColor = Color.White

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
