package com.example.medipal.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medipal.R // Cần thêm ảnh vào drawable
import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val events by viewModel.events.collectAsState(initial = emptyList())
    val isSheetVisible by viewModel.isAddSheetVisible.collectAsState()

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
                    // Giao diện Lịch (giả lập)
                    Text("February Calendar UI", color = Color.White, modifier = Modifier.padding(vertical = 20.dp))
                }

                items(events) { event ->
                    EventCard(event = event)
                }
            }
        }
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