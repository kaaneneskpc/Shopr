package com.kaaneneskpc.shopr.ui.feature.payment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.R
import com.kaaneneskpc.shopr.navigation.PaymentResultScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentVerificationScreen(
    navController: NavController,
    viewModel: PaymentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val verificationCode = remember { mutableStateOf("") }
    val isCodeValidLength = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isProcessing = remember { mutableStateOf(false) }
    val secondsLeft = remember { mutableStateOf(180) }
    val context = LocalContext.current
    val generatedCode = remember { mutableStateOf("") }
    val hasNotificationPermission = remember { mutableStateOf(checkNotificationPermission(context)) }
    val showPermissionDialog = remember { mutableStateOf(false) }
    val showAlert = remember { mutableStateOf(false) }
    

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission.value = isGranted
        if (isGranted) {
            val randomCode = (100000..999999).random().toString()
            generatedCode.value = randomCode
            viewModel.updateVerificationCode(randomCode)
            android.util.Log.d("PaymentVerification", "Permission granted, new code: $randomCode")
            createNotificationChannel(context)
            sendVerificationNotification(context, randomCode)
        } else {
            showPermissionDialog.value = true
        }
    }

    LaunchedEffect(Unit) {
        val randomCode = (100000..999999).random().toString()
        generatedCode.value = randomCode
        createNotificationChannel(context)
        
        viewModel.updateVerificationCode(randomCode)
        android.util.Log.d("PaymentVerification", "Generated code: $randomCode")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (hasNotificationPermission.value) {
                sendVerificationNotification(context, randomCode)
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            sendVerificationNotification(context, randomCode)
        }
    }
    
    if (showPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog.value = false },
            title = { Text("Notification Permission") },
            text = { 
                Text("To receive verification codes, please enable notifications for this app in settings.") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog.value = false
                        openAppSettings(context)
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    LaunchedEffect(verificationCode.value) {
        isCodeValidLength.value = verificationCode.value.length == 6
        viewModel.updateUserEnteredCode(verificationCode.value)
    }
    
    LaunchedEffect(Unit) {
        while (secondsLeft.value > 0) {
            delay(1000)
            secondsLeft.value--
        }
    }
    
    val formattedTime = remember(secondsLeft.value) {
        val minutes = secondsLeft.value / 60
        val seconds = secondsLeft.value % 60
        String.format("%02d:%02d", minutes, seconds)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navController.popBackStack() }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Secure Verification",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_done),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("3D ")
                        }
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("Secure ")
                        }
                        append("Verification")
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "A notification with your verification code has been sent. Please check your notifications and enter the 6-digit code to verify your payment.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                
                if (!hasNotificationPermission.value) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Demo code: ${generatedCode.value}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    OutlinedTextField(
                        value = verificationCode.value,
                        onValueChange = { 
                            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                verificationCode.value = it
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        placeholder = { 
                            Text(
                                text = "••••••",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = null,
                        tint = if (secondsLeft.value < 30) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Code expires in: $formattedTime",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (secondsLeft.value < 30) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = {
                        scope.launch {
                            val newCode = (100000..999999).random().toString()
                            generatedCode.value = newCode
                            viewModel.updateVerificationCode(newCode)
                            android.util.Log.d("PaymentVerification", "New code generated: $newCode")
                            
                            if (hasNotificationPermission.value) {
                                sendVerificationNotification(context, newCode)
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                sendVerificationNotification(context, newCode)
                            }
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Resend verification code",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "This is a secure connection. Your payment information is protected.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedContent(
                    targetState = isProcessing.value,
                    label = "ProcessingAnimation"
                ) { processing ->
                    if (processing) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = false
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Verifying...",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                if (isCodeValidLength.value) {
                                    val userCode = verificationCode.value
                                    val genCode = generatedCode.value
                                    android.util.Log.d("PaymentVerification", "User code: $userCode, Generated code: $genCode")
                                    
                                    if (userCode == genCode) {
                                        android.util.Log.d("PaymentVerification", "Code match successful!")
                                        scope.launch {
                                            isProcessing.value = true
                                            delay(1500)
                                            
                                            val transactionId = "TX-" + (100000..999999).random().toString()
                                            val successResult = com.kaaneneskpc.shopr.model.PaymentResult(
                                                isSuccess = true,
                                                message = "Payment processed successfully!",
                                                transactionId = transactionId
                                            )
                                            
                                            viewModel.setPaymentResult(successResult)
                                            android.util.Log.d("PaymentVerification", "Payment result set: ${successResult.isSuccess}, ID: ${successResult.transactionId}")
                                            
                                            navController.navigate(com.kaaneneskpc.shopr.navigation.PaymentResultScreen) {
                                                popUpTo(com.kaaneneskpc.shopr.navigation.PaymentVerificationScreen) { inclusive = true }
                                            }
                                        }
                                    } else {
                                        android.util.Log.d("PaymentVerification", "Code match failed!")
                                        scope.launch {
                                            showAlert.value = true
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = isCodeValidLength.value
                        ) {
                            Text(
                                text = "Verify & Complete Payment",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            title = { Text("Verification Failed") },
            text = { 
                Text("The verification code you entered doesn't match. Please check and try again.") 
            },
            confirmButton = {
                TextButton(
                    onClick = { showAlert.value = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

private fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Payment Verification"
        val descriptionText = "Payment verification code notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("PAYMENT_VERIFICATION", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

private fun sendVerificationNotification(context: Context, code: String) {
    val builder = NotificationCompat.Builder(context, "PAYMENT_VERIFICATION")
        .setSmallIcon(R.drawable.ic_done)
        .setContentTitle("Payment Verification Code")
        .setContentText("Your verification code is: $code")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
    
    with(NotificationManagerCompat.from(context)) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, builder.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 