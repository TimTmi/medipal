package com.example.medipal.presentation.ui.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medipal.R
import com.example.medipal.presentation.navigation.Screen
import com.example.medipal.presentation.viewmodel.ProfileViewModel
import com.example.medipal.presentation.viewmodel.AuthViewModel
import com.example.medipal.presentation.viewmodel.AuthState
import com.example.medipal.util.ProfileRepositoryManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = koinViewModel()
    val authViewModel: AuthViewModel = GlobalContext.get().get<AuthViewModel>()
    val context = LocalContext.current
    val profileRepositoryManager: ProfileRepositoryManager = koinInject<ProfileRepositoryManager>()
    
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    val authState by authViewModel.authState.collectAsState()
    
    // Listen for auth state changes
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            viewModel.resetProfile()
            Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT).show()
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
                    title = { Text("Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                // Profile Avatar
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // User Name
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = uiState.userName,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(60.dp))
                
                // Action Buttons
                ProfileActionButton(
                    icon = Icons.Default.Edit,
                    text = "Edit Profile",
                    onClick = { 
                        navController.navigate("edit_profile")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfileActionButton(
                    icon = Icons.Default.SupervisorAccount,
                    text = "Manage Caregiver Access",
                    onClick = { 
                        navController.navigate(Screen.ManageCaregiverAccess.route)
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfileActionButton(
                    icon = Icons.Default.ExitToApp,
                    text = "Log out",
                    onClick = { 
                        showLogoutDialog = true
                    }
                )
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        
        // Logout confirmation dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to logout?",
                        color = Color.Black
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            authViewModel.signOut()
                            showLogoutDialog = false
                            profileRepositoryManager.setCurrentProfile("default-profile")
                        }
                    ) {
                        Text(
                            text = "Logout",
                            color = Color(0xFF1C5F55),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Gray
                        )
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun ProfileActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E5A3E).copy(alpha = 0.8f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
