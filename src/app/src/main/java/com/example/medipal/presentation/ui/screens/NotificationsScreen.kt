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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medipal.MediPalApplication
import com.example.medipal.R
import com.example.medipal.domain.model.NotificationItem
import com.example.medipal.domain.model.NotificationStatus
import com.example.medipal.domain.model.NotificationType
import com.example.medipal.presentation.viewmodel.NotificationViewModel
import com.example.medipal.presentation.viewmodel.ViewModelFactory
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as MediPalApplication
    val viewModelFactory = ViewModelFactory(application.container)
    val viewModel: NotificationViewModel = viewModel(factory = viewModelFactory)
    
    val uiState by viewModel.uiState.collectAsState()
    
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
                    title = { Text("Notifications", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // Today Section
                    if (uiState.todayNotifications.isNotEmpty()) {
                        item {
                            NotificationSectionHeader(title = "Today")
                        }
                        
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column {
                                    uiState.todayNotifications.forEachIndexed { index, notification ->
                                        NotificationItemCard(
                                            notification = notification,
                                            onClick = {
                                                when (notification.status) {
                                                    NotificationStatus.MISSED -> {
                                                        navController.navigate("missed_dose_detail/${notification.id}")
                                                    }
                                                    NotificationStatus.UPCOMING -> {
                                                        navController.navigate("upcoming_dose_detail/${notification.id}")
                                                    }
                                                    else -> {}
                                                }
                                            }
                                        )
                                        
                                        if (index < uiState.todayNotifications.size - 1) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(horizontal = 16.dp),
                                                color = Color.Gray.copy(alpha = 0.3f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Yesterday Section
                    if (uiState.yesterdayNotifications.isNotEmpty()) {
                        item {
                            NotificationSectionHeader(title = "Yesterday")
                        }
                        
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column {
                                    uiState.yesterdayNotifications.forEachIndexed { index, notification ->
                                        NotificationItemCard(
                                            notification = notification,
                                            onClick = {
                                                when (notification.status) {
                                                    NotificationStatus.MISSED -> {
                                                        navController.navigate("missed_dose_detail/${notification.id}")
                                                    }
                                                    NotificationStatus.UPCOMING -> {
                                                        navController.navigate("upcoming_dose_detail/${notification.id}")
                                                    }
                                                    else -> {}
                                                }
                                            }
                                        )
                                        
                                        if (index < uiState.yesterdayNotifications.size - 1) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(horizontal = 16.dp),
                                                color = Color.Gray.copy(alpha = 0.3f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Empty state
                    if (uiState.todayNotifications.isEmpty() && uiState.yesterdayNotifications.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "No notifications",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Your medication reminders will appear here",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationSectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun NotificationItemCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = when (notification.status) {
                        NotificationStatus.MISSED -> Color(0xFFE57373)
                        NotificationStatus.UPCOMING -> Color(0xFF4CAF50)
                        else -> Color.Gray
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (notification.type) {
                    NotificationType.MEDICATION -> when (notification.status) {
                        NotificationStatus.MISSED -> Icons.Default.Cancel
                        NotificationStatus.UPCOMING -> Icons.Default.AccessTime
                        else -> Icons.Default.CheckCircle
                    }
                    NotificationType.APPOINTMENT -> Icons.Default.Person
                    NotificationType.REMINDER -> Icons.Default.AccessTime
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Notification Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = when (notification.status) {
                    NotificationStatus.MISSED -> "Missed ${notification.subtitle}"
                    NotificationStatus.UPCOMING -> "Upcoming ${notification.subtitle}"
                    else -> notification.subtitle
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            
            Text(
                text = "Time: ${notification.time} | ${notification.title}",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
