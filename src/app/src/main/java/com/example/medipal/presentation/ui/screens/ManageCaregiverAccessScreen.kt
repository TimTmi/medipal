package com.example.medipal.presentation.ui.screens

import com.example.medipal.presentation.viewmodel.ManageCaregiverViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import com.example.medipal.R
import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.AccountType
import com.example.medipal.domain.model.CaregiverAssignment
import com.example.medipal.domain.service.AccountService
import com.example.medipal.presentation.navigation.Screen
import org.koin.compose.koinInject


@Composable
fun ManageCaregiverAccessScreen(
    navController: NavController,
    viewModel: ManageCaregiverViewModel
) {
    val accountService: AccountService = koinInject()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    
    // State for current account
    var currentAccount by remember { mutableStateOf<Account?>(null) }
    
    // Get current account and initialize viewModel
    LaunchedEffect(Unit) {
        val account = accountService.getCurrentAccount()
        if (account != null) {
            currentAccount = account
            viewModel.initialize(account)
        }
    }
    
    val assignments by viewModel.assignments.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var newId by remember { mutableStateOf("") }
    
    // Use the current account from state
    val account = currentAccount

    val isCustomer = account?.type == AccountType.CUSTOMER
    val linkedIds = if (isCustomer == true) {
        assignments.map { it.caregiverId }
    } else {
        assignments.map { it.customerId }
    }

    val brightness = 0.5f
    val colorMatrix = ColorMatrix().apply {
        setToScale(brightness, brightness, brightness, 1f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.forest_background),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.90f),
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Manage Caregiver Access", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Show current account info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Your Account",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Clickable Account ID row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            account?.id?.let { accountId ->
                                clipboardManager.setText(AnnotatedString(accountId))
                                Toast.makeText(context, "Account ID copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ID: ${account?.id ?: "Loading..."}",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (account?.id != null) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Account ID",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Text(
                    text = "Email: ${account?.email ?: "Loading..."}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Type: ${account?.type?.name ?: "Loading..."}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Linked caregivers/customers
        Text(
            text = if (isCustomer) "Your Caregivers" else "Your Customers",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val linkedAccountsWithNames by viewModel.linkedAccountsWithNames.collectAsState()
        
        if (linkedAccountsWithNames.isEmpty()) {
            Text("No linked accounts", color = Color.White.copy(alpha = 0.7f))
        } else {
            linkedAccountsWithNames.forEach { (accountId, profileName) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = profileName,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(accountId))
                                        Toast.makeText(context, "Account ID copied to clipboard", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ID: ${accountId.take(12)}...",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Account ID",
                                    tint = Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.removeLink(accountId) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add new account ID
        OutlinedTextField(
            value = newId,
            onValueChange = { newId = it },
            label = { Text("Add by Account ID", color = Color.White.copy(alpha = 0.8f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add",
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (newId.isNotBlank()) {
                    viewModel.addLink(newId.trim())
                    newId = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C5F55)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add", color = Color.White)
        }
            }
        }
    }
}
