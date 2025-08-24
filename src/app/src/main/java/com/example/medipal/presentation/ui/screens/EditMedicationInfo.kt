package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.viewmodel.MedicationDetailViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.medipal.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicineScreen(
    navController: NavController,
    viewModel: MedicationDetailViewModel
) {
    val medicineName by viewModel.medicineName.collectAsState()
    val instructions by viewModel.description.collectAsState()
    val selectedFrequencyObject by viewModel.selectedFrequencyObject.collectAsState()
    val darkGreen = Color(0xFF1C5F55)
    val whiteColor = Color.White

    // --- BỐ CỤC CHÍNH ---
    // Sử dụng Column thay vì Scaffold
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- PHẦN HEADER MÀU XANH ---
        // Sử dụng Box để định vị các nút và tiêu đề
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Đặt một chiều cao cố định cho header
                .height(80.dp)
                .background(darkGreen),
            // Căn giữa cho tiêu đề mặc định
            contentAlignment = Alignment.Center
        ) {
            // Nút Cancel
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart) // Căn giữa theo chiều dọc, bên trái
                    .padding(start = 8.dp)
            ) {
                Text("Cancel", color = whiteColor)
            }

            // Tiêu đề "Edit Medicines"
            Text(
                text = "Edit Medicines",
                color = whiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Nút Update
            TextButton(
                onClick = {
                    viewModel.onUpdate {
                        navController.navigate(Screen.Medications.route) {
                            popUpTo(Screen.Medications.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Căn giữa theo chiều dọc, bên phải
                    .padding(end = 8.dp)
            ) {
                Text("Update", color = whiteColor)
            }
        }

        // --- PHẦN NỘI DUNG CHÍNH (FORM) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Label cho TextField
            Text(
                text = "Medicine",
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
            OutlinedTextField(
                value = medicineName,
                onValueChange = { viewModel.medicineName.value = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Instructions",
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
            OutlinedTextField(
                value = instructions,
                onValueChange = { viewModel.description.value = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Reminders",
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Chiều cao tiêu chuẩn của TextField
                    .clickable { // Thêm sự kiện click vào đây
                        navController.navigate(Screen.EditMedicineFrequency.route)
                    },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline) // Tạo viền
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Text hiển thị giá trị
                    Text(
                        text = selectedFrequencyObject.displayText,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Icon mũi tên
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Select frequency",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Dùng Spacer với weight để đẩy nút Delete xuống dưới cùng
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.onDelete {
                        navController.navigate(Screen.Medications.route) {
                            popUpTo(Screen.Medications.route) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Delete")
            }
        }
    }
}