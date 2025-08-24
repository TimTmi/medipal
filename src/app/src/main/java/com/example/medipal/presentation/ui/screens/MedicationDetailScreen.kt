package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medipal.R // Đảm bảo bạn có icon ic_pill trong drawable
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.viewmodel.MedicationDetailViewModel
import java.text.SimpleDateFormat
import androidx.compose.foundation.shape.RoundedCornerShape
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    navController: NavController,
    viewModel: MedicationDetailViewModel
) {
    val medication by viewModel.medication.collectAsState()

    // --- ĐỊNH NGHĨA MÀU SẮC ---
    val lightGreen = Color(0xFFA7BEB5)
    val whiteTextAndIcon = Color.White
    val darkGreenText = Color(0xFF1C5F55) // Có thể dùng cho phần nội dung nếu muốn

    // --- BỐ CỤC CHÍNH ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Màu nền trắng cho toàn bộ màn hình
    ) {
        // --- PHẦN HEADER MÀU XANH ---
        // Sử dụng Box để dễ dàng định vị các thành phần con
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Tăng chiều cao của vùng header để có không gian cho các nút
                .height(250.dp)
                .background(lightGreen)
        ) {
            // Nút Cancel
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart) // Căn lề trên cùng bên trái
                    .padding(top = 16.dp, start = 8.dp) // Đẩy nút vào một chút
            ) {
                Text("Cancel", color = whiteTextAndIcon)
            }

            // Nút Edit
            TextButton(
                onClick = {
                    medication?.id?.let {
                        navController.navigate(Screen.EditMedicine.createRoute(it))
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd) // Căn lề trên cùng bên phải
                    .padding(top = 16.dp, end = 8.dp) // Đẩy nút vào một chút
            ) {
                Text("Edit", color = whiteTextAndIcon)
            }

            // Phần nội dung chính của Header (Icon và Tên thuốc)
            // Hiển thị khi dữ liệu đã được tải
            medication?.let { med ->
                Column(
                    modifier = Modifier.align(Alignment.Center), // Căn giữa Box
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon viên thuốc - LÀM TO HƠN
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pill),
                        contentDescription = "Medication Icon",
                        // Tăng kích thước icon lên đáng kể
                        modifier = Modifier.size(96.dp),
                        tint = whiteTextAndIcon
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Tên thuốc
                    Text(
                        text = med.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = whiteTextAndIcon
                    )
                }
            }
        }

        // --- PHẦN NỘI DUNG TRẮNG ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Hiển thị loading indicator nếu dữ liệu chưa có
            if (medication == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Hiển thị thông tin chi tiết
                DetailItem(
                    title = "Instructions",
                    content = medication!!.notes ?: "No instructions provided."
                )
                Spacer(modifier = Modifier.height(24.dp))
                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val formattedTime = timeFormatter.format(Date(medication!!.scheduleTime))
                val frequencyText = medication!!.frequency.displayText

                DetailItem(title = "Reminders", content = "$frequencyText, $formattedTime")
            }
        }

    }
}

/**
 * Composable phụ để hiển thị một mục thông tin chi tiết (tiêu đề + nội dung)
 * giúp tái sử dụng code và làm cho giao diện chính sạch sẽ hơn.
 */
@Composable
fun DetailItem(title: String, content: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = 18.sp
    )
}

/**
 * Hàm tiện ích để chuyển đổi timestamp (Long) thành chuỗi giờ:phút AM/PM.
 */
