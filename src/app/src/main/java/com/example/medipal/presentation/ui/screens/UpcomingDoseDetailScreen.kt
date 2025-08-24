package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medipal.R
import com.example.medipal.domain.model.NotificationType
import com.example.medipal.presentation.viewmodel.NotificationViewModel
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingDoseDetailScreen(
    navController: NavController,
    notificationId: String
) {
    val viewModel: NotificationViewModel = koinViewModel()
    
    val notification = viewModel.getNotificationById(notificationId)
    
    // Countdown timer state
    var timeRemaining by remember { mutableLongStateOf(0L) }
    var isExpired by remember { mutableStateOf(false) }
    
    // Calculate initial time remaining
    LaunchedEffect(notification) {
        if (notification != null) {
            val currentTime = System.currentTimeMillis()
            val scheduledTime = notification.scheduleTime
            timeRemaining = if (scheduledTime > currentTime) {
                scheduledTime - currentTime
            } else {
                0L
            }
            isExpired = timeRemaining <= 0L
        }
    }
    
    // Countdown effect
    LaunchedEffect(timeRemaining) {
        if (timeRemaining > 0) {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining -= 1000L
                if (timeRemaining <= 0) {
                    isExpired = true
                    // Navigate to missed dose screen when timer expires
                    navController.navigate("missed_dose_detail/$notificationId") {
                        popUpTo("upcoming_dose_detail/$notificationId") { inclusive = true }
                    }
                }
            }
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
            modifier = Modifier.fillMaxSize()
                .alpha(0.90f),
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = when (notification?.type) {
                                NotificationType.MEDICATION -> "Upcoming Dose"
                                NotificationType.APPOINTMENT -> "Upcoming Appointment"
                                NotificationType.REMINDER -> "Upcoming Reminder"
                                else -> "Upcoming Item"
                            },
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            if (notification != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Medication/Item Details Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Title
                            Text(
                                text = notification.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Time
                            Text(
                                text = "Time: ${notification.time}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Status
                            Text(
                                text = if (isExpired) "Expired" else "Coming",
                                fontSize = 16.sp,
                                color = if (isExpired) Color(0xFFE57373) else Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Divider
                            HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Instructions
                            Text(
                                text = "Instructions",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = when (notification.type) {
                                    NotificationType.MEDICATION -> notification.instructions.ifEmpty { "Take as prescribed" }
                                    NotificationType.APPOINTMENT -> "Appointment with ${notification.doctor}"
                                    NotificationType.REMINDER -> "Complete your reminder task"
                                },
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    // Countdown Timer
                    if (!isExpired) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = formatTime(timeRemaining),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } else {
                // Error state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Notification not found",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigateUp() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Go Back", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(milliseconds: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
    
    return if (hours > 0) {
        String.format("%02d:%02d:%02ds", hours, minutes, seconds)
    } else {
        String.format("%02d:%02ds", minutes, seconds)
    }
}
