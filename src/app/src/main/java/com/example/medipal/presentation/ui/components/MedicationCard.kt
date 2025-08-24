package com.example.medipal.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medipal.domain.model.Medication
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MedicationCard(
    medication: Medication,
    onClick: () -> Unit
) {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = timeFormatter.format(Date(medication.scheduleTime))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medication.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = formattedTime,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Dosage: ${medication.dosage}",
                fontSize = 14.sp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Frequency: ${medication.frequency.displayText}",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f)
            )
            
            if (medication.notes != "") {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Notes: ${medication.notes}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
