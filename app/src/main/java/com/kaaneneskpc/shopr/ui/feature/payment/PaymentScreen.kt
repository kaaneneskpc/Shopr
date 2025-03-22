package com.kaaneneskpc.shopr.ui.feature.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.model.PaymentModel
import com.kaaneneskpc.shopr.navigation.PaymentVerificationScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PaymentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val paymentModel = remember { mutableStateOf(PaymentModel()) }
    val formValid = remember { mutableStateOf(false) }
    
    LaunchedEffect(paymentModel.value) {
        formValid.value = isFormValid(paymentModel.value)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Payment Details",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
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
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = if (paymentModel.value.cardNumber.isNotEmpty()) 
                                   formatCardNumber(paymentModel.value.cardNumber) 
                                   else "•••• •••• •••• ••••",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "CARD HOLDER",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = paymentModel.value.cardHolderName.ifEmpty { "YOUR NAME" },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "EXPIRES",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = paymentModel.value.expiryDate.ifEmpty { "MM/YY" },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = paymentModel.value.cardHolderName,
                    onValueChange = { 
                        paymentModel.value = paymentModel.value.copy(cardHolderName = it)
                    },
                    label = { Text("Card Holder Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = paymentModel.value.cardNumber,
                    onValueChange = { 
                        val digitsOnly = it.replace(" ", "")
                        if (digitsOnly.length <= 16 && digitsOnly.all { char -> char.isDigit() }) {
                            val formatted = formatCardNumberInput(digitsOnly)
                            paymentModel.value = paymentModel.value.copy(cardNumber = digitsOnly)
                        }
                    },
                    label = { Text("Card Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    placeholder = { Text("1234 5678 9012 3456") },
                    visualTransformation = { text ->
                        val formattedText = formatCardNumberInput(text.text)
                        TransformedText(
                            AnnotatedString(formattedText),
                            creditCardOffsetTranslator(text.text)
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = paymentModel.value.expiryDate,
                        onValueChange = { 
                            val input = it.replace("/", "")
                            if (input.length <= 4 && input.all { char -> char.isDigit() }) {
                                val formatted = when {
                                    input.length <= 2 -> input
                                    else -> "${input.substring(0, 2)}/${input.substring(2)}"
                                }
                                paymentModel.value = paymentModel.value.copy(expiryDate = formatted)
                            }
                        },
                        label = { Text("Expiry Date") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        placeholder = { Text("MM/YY") }
                    )
                    
                    OutlinedTextField(
                        value = paymentModel.value.cvv,
                        onValueChange = { 
                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                paymentModel.value = paymentModel.value.copy(cvv = it)
                            }
                        },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        placeholder = { Text("123") }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Your card information is encrypted and secure",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))

                AnimatedVisibility(
                    visible = formValid.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = {
                            viewModel.setPaymentInfo(paymentModel.value)
                            navController.navigate(PaymentVerificationScreen)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Proceed to Verification",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                AnimatedVisibility(
                    visible = !formValid.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        enabled = false
                    ) {
                        Text(
                            text = "Complete Card Details",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

private fun formatCardNumber(number: String): String {
    val formatted = StringBuilder()
    
    for (i in number.indices) {
        if (i > 0 && i % 4 == 0) {
            formatted.append(" ")
        }
        formatted.append(number[i])
    }
    
    return formatted.toString()
}

private fun formatCardNumberInput(text: String): String {
    val digitsOnly = text.replace(" ", "")
    val formatted = StringBuilder()
    
    for (i in digitsOnly.indices) {
        if (i > 0 && i % 4 == 0) {
            formatted.append(" ")
        }
        formatted.append(digitsOnly[i])
    }
    
    return formatted.toString()
}

private fun isFormValid(paymentModel: PaymentModel): Boolean {
    return paymentModel.cardHolderName.isNotBlank() &&
            paymentModel.cardNumber.length == 16 &&
            paymentModel.expiryDate.length == 5 &&
            paymentModel.cvv.length == 3
}

private fun creditCardOffsetTranslator(text: String): androidx.compose.ui.text.input.OffsetMapping {
    val spacesIndices = mutableListOf<Int>()
    val digitsOnly = text.replace(" ", "")

    for (i in 1 until digitsOnly.length) {
        if (i % 4 == 0) {
            spacesIndices.add(i)
        }
    }
    
    return object : androidx.compose.ui.text.input.OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val spacesBeforeOffset = spacesIndices.count { it < offset }
            return offset + spacesBeforeOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            var spacesBeforeOffset = 0
            for (spaceIndex in spacesIndices) {
                val transformedSpaceIndex = spaceIndex + spacesBeforeOffset
                if (transformedSpaceIndex < offset) {
                    spacesBeforeOffset++
                } else {
                    break
                }
            }
            return offset - spacesBeforeOffset
        }
    }
} 