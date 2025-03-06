package com.kaaneneskpc.shopr.ui.feature.profile.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kaaneneskpc.domain.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    userProfile: UserProfile
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var email by remember { mutableStateOf(userProfile.email) }
    var phoneNumber by remember { mutableStateOf(userProfile.phoneNumber) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Düzenle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // Profil bilgilerini güncelle ve geri dön
                            val updatedProfile = userProfile.copy(
                                name = name,
                                email = email,
                                phoneNumber = phoneNumber
                            )
                            // TODO: ViewModel üzerinden profil güncelleme işlemi yapılacak
                            navController.popBackStack()
                        }
                    ) {
                        Text("Kaydet")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profil resmi
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil Resmi",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                
                FloatingActionButton(
                    onClick = { /* Profil resmi değiştirme işlemi */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Profil Resmini Değiştir",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Form alanları
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ad Soyad") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Telefon Numarası") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    val updatedProfile = userProfile.copy(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                    // TODO: ViewModel üzerinden profil güncelleme işlemi yapılacak
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Değişiklikleri Kaydet",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
} 