package com.kaaneneskpc.shopr.ui.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.model.UiProductModel
import com.kaaneneskpc.shopr.navigation.ProductDetails
import com.kaaneneskpc.shopr.ui.feature.home.components.HomeCategoriesRow
import com.kaaneneskpc.shopr.ui.feature.home.components.HomeProductRow
import com.kaaneneskpc.shopr.ui.feature.home.components.ProfileHeader
import com.kaaneneskpc.shopr.ui.feature.home.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var searchQuery by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ProfileHeader()
                SearchBar(
                    value = searchQuery,
                    onTextChanged = { searchQuery = it },
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            when (uiState) {
                is HomeScreenEvent.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading products...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                is HomeScreenEvent.Success -> {
                    if (uiState.categories.isNotEmpty()) {
                        item {
                            HomeCategoriesRow(
                                categories = uiState.categories,
                                title = "Categories",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    if (uiState.featured.isNotEmpty()) {
                        item {
                            HomeProductRow(
                                products = uiState.featured,
                                title = "Featured Products",
                                onClick = { product ->
                                    navController.navigate(ProductDetails(UiProductModel.fromProduct(product)))
                                }
                            )
                        }
                    }

                    if (uiState.popularProducts.isNotEmpty()) {
                        item {
                            HomeProductRow(
                                products = uiState.popularProducts,
                                title = "Popular Products",
                                onClick = { product ->
                                    navController.navigate(ProductDetails(UiProductModel.fromProduct(product)))
                                }
                            )
                        }
                    }
                }

                is HomeScreenEvent.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Oops! Something went wrong",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.getAllProducts() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Try Again")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}