package com.kaaneneskpc.shopr.ui.feature.summary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.model.UserAddress
import com.kaaneneskpc.shopr.navigation.UserAddressRoute
import com.kaaneneskpc.shopr.navigation.UserAddressRouteWrapper
import com.kaaneneskpc.shopr.ui.feature.summary.components.AddressBar
import com.kaaneneskpc.shopr.ui.feature.summary.components.CartSummaryScreenContent
import com.kaaneneskpc.shopr.ui.feature.userAddress.USER_ADDRESS_SCREEN
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSummaryScreen(
    navController: NavController,
    viewModel: CartSummaryViewModel = koinViewModel()
) {
    val address = remember { mutableStateOf<UserAddress?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cart Summary",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
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
                    .padding(16.dp)
            ) {
                val uiState = viewModel.uiState.collectAsState()
                LaunchedEffect(navController) {
                    val savedState = navController.currentBackStackEntry?.savedStateHandle
                    savedState?.getStateFlow(USER_ADDRESS_SCREEN, address.value)?.collect { userAddress ->
                        address.value = userAddress
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (val event = uiState.value) {
                        is CartSummaryEvent.Loading -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading your cart...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        is CartSummaryEvent.Error -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Oops!",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = event.error,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        is CartSummaryEvent.Success -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    AddressBar(address.value?.toString() ?: "", onClick = {
                                        navController.navigate(UserAddressRoute(UserAddressRouteWrapper(address.value)))
                                    })
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    CartSummaryScreenContent(event.summary)
                                }
                            }
                        }
                    }
                }
                
                ElevatedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Proceed to Checkout",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}