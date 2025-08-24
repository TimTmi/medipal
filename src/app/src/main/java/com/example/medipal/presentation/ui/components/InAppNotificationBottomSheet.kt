package com.example.medipal.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medipal.domain.model.NotificationItem
import com.example.medipal.domain.model.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppNotificationBottomSheet(
    notification: NotificationItem?,
    onDismiss: () -> Unit,
    onTaken: () -> Unit,
    onSkipped: () -> Unit
) {
    if (notification != null) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Notification Icon
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notification",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Title
                Text(
                    text = when (notification.type) {
                        NotificationType.MEDICATION -> "Medication Reminder"
                        NotificationType.APPOINTMENT -> "Appointment Reminder"
                        NotificationType.REMINDER -> "Health Reminder"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Notification Details
                Text(
                    text = notification.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2E7D32)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Time: ${notification.time}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                if (notification.instructions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = notification.instructions,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Mark as Taken Button
                    Button(
                        onClick = {
                            onTaken()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Mark as Taken",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (notification.type) {
                                NotificationType.MEDICATION -> "Taken"
                                NotificationType.APPOINTMENT -> "Done"
                                NotificationType.REMINDER -> "Done"
                            },
                            fontSize = 16.sp
                        )
                    }
                    
                    // Mark as Skipped Button
                    Button(
                        onClick = {
                            onSkipped()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE57373),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Mark as Skipped",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (notification.type) {
                                NotificationType.MEDICATION -> "Skip"
                                NotificationType.APPOINTMENT -> "Miss"
                                NotificationType.REMINDER -> "Skip"
                            },
                            fontSize = 16.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
