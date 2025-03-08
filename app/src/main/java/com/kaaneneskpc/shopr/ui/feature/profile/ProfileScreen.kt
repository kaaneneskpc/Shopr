package com.kaaneneskpc.shopr.ui.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.shopr.model.toUserAddress
import com.kaaneneskpc.shopr.navigation.EditProfileRoute
import com.kaaneneskpc.shopr.navigation.OrdersScreen
import com.kaaneneskpc.shopr.navigation.UserAddressRoute
import com.kaaneneskpc.shopr.navigation.UserAddressRouteWrapper
import com.kaaneneskpc.shopr.ui.feature.profile.components.ProfileHeader
import com.kaaneneskpc.shopr.ui.feature.profile.components.ProfileMenuItem
import com.kaaneneskpc.shopr.ui.feature.profile.components.ProfileSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val passwordUpdateState by viewModel.passwordUpdateState.collectAsStateWithLifecycle()

    var showPasswordDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Bir hata oluştu",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = (uiState as ProfileUiState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.getUserProfile() },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tekrar Dene")
                        }
                    }
                }
            }

            is ProfileUiState.Success -> {
                val userProfile = (uiState as ProfileUiState.Success).userProfile
                ProfileContent(
                    userProfile = userProfile,
                    onEditProfileClick = {
                        navController.navigate(EditProfileRoute(userProfile))
                    },
                    onAddressesClick = {
                        val userAddress = userProfile.defaultAddress?.toUserAddress()
                        navController.navigate(UserAddressRoute(UserAddressRouteWrapper(userAddress)))
                    },
                    onPasswordChangeClick = { showPasswordDialog = true },
                    onNotificationsClick = { },
                    onLanguageClick = { },
                    onThemeClick = { },
                    onLogoutClick = { showLogoutDialog = true },
                    onOrdersClick = { navController.navigate(OrdersScreen) }
                )
            }
        }
    }

    // Şifre değiştirme dialog'u
    if (showPasswordDialog) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showPasswordDialog = false
                viewModel.resetPasswordUpdateState()
            },
            title = { Text("Şifre Değiştir") },
            text = {
                Column {
                    when (passwordUpdateState) {
                        is PasswordUpdateState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }

                        is PasswordUpdateState.Error -> {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = (passwordUpdateState as PasswordUpdateState.Error).message,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        is PasswordUpdateState.Success -> {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Şifreniz başarıyla değiştirildi!",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        else -> { /* Idle state, do nothing */
                        }
                    }

                    if (passwordUpdateState !is PasswordUpdateState.Success) {
                        OutlinedTextField(
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            label = { Text("Mevcut Şifre") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = passwordUpdateState is PasswordUpdateState.Error,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Yeni Şifre") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = passwordUpdateState is PasswordUpdateState.Error,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Yeni Şifre (Tekrar)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = passwordUpdateState is PasswordUpdateState.Error || (confirmPassword.isNotEmpty() && confirmPassword != newPassword),
                            shape = RoundedCornerShape(8.dp)
                        )

                        if (confirmPassword.isNotEmpty() && confirmPassword != newPassword) {
                            Text(
                                text = "Şifreler eşleşmiyor",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (passwordUpdateState is PasswordUpdateState.Success) {
                    Button(
                        onClick = {
                            showPasswordDialog = false
                            viewModel.resetPasswordUpdateState()
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Tamam")
                    }
                } else {
                    Button(
                        onClick = {
                            if (newPassword == confirmPassword && newPassword.isNotEmpty()) {
                                viewModel.updatePassword(oldPassword, newPassword)
                            }
                        },
                        enabled = oldPassword.isNotEmpty() && newPassword.isNotEmpty() &&
                                confirmPassword.isNotEmpty() && newPassword == confirmPassword &&
                                passwordUpdateState !is PasswordUpdateState.Loading,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Değiştir")
                    }
                }
            },
            dismissButton = {
                if (passwordUpdateState !is PasswordUpdateState.Success) {
                    OutlinedButton(
                        onClick = {
                            showPasswordDialog = false
                            viewModel.resetPasswordUpdateState()
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("İptal")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Çıkış yapma onay dialog'u
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Çıkış Yap") },
            text = {
                Column {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )
                    Text("Hesabınızdan çıkış yapmak istediğinize emin misiniz?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        // Çıkış yapma işlemi
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Evet, Çıkış Yap")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("İptal")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun ProfileContent(
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit,
    onAddressesClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onPasswordChangeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(
            userProfile = userProfile,
            onEditClick = onEditProfileClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileSection(title = "Hesap") {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = "Profil Bilgileri",
                    subtitle = "Kişisel bilgilerinizi düzenleyin",
                    onClick = onEditProfileClick
                )

                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Adreslerim",
                    subtitle = "Teslimat adreslerinizi yönetin",
                    onClick = onAddressesClick
                )

                ProfileMenuItem(
                    icon = Icons.Default.ShoppingCart,
                    title = "Siparişlerim",
                    subtitle = "Siparişlerinizi yönetin",
                    onClick = onOrdersClick
                )

                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Şifre Değiştir",
                    subtitle = "Hesap güvenliğinizi güncelleyin",
                    onClick = onPasswordChangeClick
                )
            }
        }

        ProfileSection(title = "Ayarlar") {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileMenuItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Bildirim Tercihleri",
                    subtitle = "Bildirim ayarlarınızı yönetin",
                    onClick = onNotificationsClick
                )

                ProfileMenuItem(
                    icon = Icons.Outlined.Edit,
                    title = "Dil",
                    subtitle = "Uygulama dilini değiştirin",
                    onClick = onLanguageClick
                )

                ProfileMenuItem(
                    icon = Icons.Outlined.Build,
                    title = "Tema",
                    subtitle = "Açık/koyu tema seçin",
                    onClick = onThemeClick
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Çıkış Yap",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}