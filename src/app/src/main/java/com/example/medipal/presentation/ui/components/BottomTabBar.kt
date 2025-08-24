// Trong file BottomTabBar.kt
package com.example.medipal.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medipal.presentation.navigation.Screen

// THAY ĐỔI 1: Đơn giản hóa data class, chỉ cần 1 icon vì luôn dùng dạng Outlined
data class BottomNavItem(
    val route: String,
    val icon: ImageVector, // Chỉ cần một icon
    val label: String,
    val hasNotification: Boolean = false,
    val notificationCount: Int = 0
)

@Composable
fun BottomTabBar(
    navController: NavController,
    currentRoute: String?
) {
    // THAY ĐỔI 2: Định nghĩa màu xanh teal cho tab được chọn
    val selectedColor = Color(0xFF00A99D)

    // THAY ĐỔI 3: Cập nhật danh sách items để sử dụng icon Outlined và icon Pill mới
    val items = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            icon = Icons.Outlined.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = Screen.Medications.route,
            // Sử dụng icon viên thuốc từ thư viện mở rộng
            icon = Icons.Outlined.Medication,
            label = "Medicines"
        ),
        BottomNavItem(
            route = Screen.AppointmentReminder.route,
            icon = Icons.Outlined.CalendarMonth,
            label = "Calendar"
        ),
        BottomNavItem(
            route = Screen.Notifications.route,
            icon = Icons.Outlined.Notifications,
            label = "Alerts",
            hasNotification = true,
            notificationCount = 9
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            icon = Icons.Outlined.AccountCircle,
            label = "Profile"
        )
    )

    val bottomBarColor = Color(0xFF28463a)

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        containerColor = bottomBarColor,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    BadgedBox(
                        modifier = Modifier.padding(top = 6.dp),
                        badge = {
                            if (item.hasNotification && item.notificationCount > 0) {
                                Badge {
                                    Text(text = if (item.notificationCount > 9) "9+" else item.notificationCount.toString())
                                }
                            }
                        }
                    ) {
                        // THAY ĐỔI 4: Luôn dùng `item.icon`
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    }
                },
                // THAY ĐỔI 5: Cập nhật màu sắc theo yêu cầu mới
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor, // Màu xanh teal khi được chọn
                    unselectedIconColor = Color.White.copy(alpha = 0.9f), // Màu trắng cho các tab còn lại
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}