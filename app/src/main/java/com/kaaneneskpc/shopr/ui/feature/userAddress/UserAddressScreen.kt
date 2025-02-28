package com.kaaneneskpc.shopr.ui.feature.userAddress

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.model.UserAddress

const val USER_ADDRESS_SCREEN = "user_address"

@Composable
fun UserAddressScreen(navController: NavController, userAddress: UserAddress?) {
    val addressLine = remember { mutableStateOf(userAddress?.addressLine ?: "") }
    val city = remember { mutableStateOf(userAddress?.city ?: "") }
    val state = remember { mutableStateOf(userAddress?.state ?: "") }
    val postalCode = remember { mutableStateOf(userAddress?.postalCode ?: "") }
    val country = remember { mutableStateOf(userAddress?.country ?: "") }
    
    val scrollState = rememberScrollState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    val hasAnyContent = addressLine.value.isNotEmpty() || 
                       city.value.isNotEmpty() || 
                       state.value.isNotEmpty() || 
                       postalCode.value.isNotEmpty() || 
                       country.value.isNotEmpty()
    
    fun clearAllFields() {
        addressLine.value = ""
        city.value = ""
        state.value = ""
        postalCode.value = ""
        country.value = ""
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Address",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Do you want to delete address?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        clearAllFields()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        "Yes",
                        color = Color.Blue,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        "No",
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Text(
                    text = "Address Details",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                IconButton(
                    onClick = { showDeleteDialog = true },
                    enabled = hasAnyContent
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Clear All Fields",
                        tint = if (hasAnyContent) Color.Red else Color.Gray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = addressLine.value,
                onValueChange = { addressLine.value = it },
                label = { Text("Street Address") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline
                ),
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = city.value,
                onValueChange = { city.value = it },
                label = { Text("City") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline
                ),
                shape = MaterialTheme.shapes.medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.value,
                    onValueChange = { state.value = it },
                    label = { Text("State") },
                    leadingIcon = { 
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = MaterialTheme.shapes.medium
                )

                OutlinedTextField(
                    value = postalCode.value,
                    onValueChange = { postalCode.value = it },
                    label = { Text("Post Code") },
                    leadingIcon = { 
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }

            OutlinedTextField(
                value = country.value,
                onValueChange = { country.value = it },
                label = { Text("Country") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            val isFormValid = addressLine.value.isNotEmpty() && 
                            city.value.isNotEmpty() && 
                            state.value.isNotEmpty() && 
                            postalCode.value.isNotEmpty() && 
                            country.value.isNotEmpty()

            Button(
                onClick = {
                    val address = UserAddress(
                        addressLine = addressLine.value,
                        city = city.value,
                        state = state.value,
                        postalCode = postalCode.value,
                        country = country.value
                    )
                    val previousBackStack = navController.previousBackStackEntry
                    previousBackStack?.savedStateHandle?.set(USER_ADDRESS_SCREEN, address)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Save Address",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            AnimatedVisibility(
                visible = !isFormValid,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Please fill in all fields to continue",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}