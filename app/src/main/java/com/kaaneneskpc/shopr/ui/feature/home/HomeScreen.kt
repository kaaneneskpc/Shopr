package com.kaaneneskpc.shopr.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.domain.model.Product
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
    val loading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }
    val featured = remember { mutableStateOf<List<Product>>(emptyList()) }
    val popularProducts = remember { mutableStateOf<List<Product>>(emptyList()) }
    val categories = remember { mutableStateOf<List<String>>(emptyList()) }


    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (uiState) {
                is HomeScreenEvent.Loading -> {
                    loading.value = true
                    error.value = null
                }

                is HomeScreenEvent.Success -> {
                    featured.value = uiState.featured
                    popularProducts.value = uiState.popularProducts
                    categories.value = uiState.categories
                    loading.value = false
                    error.value = null
                }

                is HomeScreenEvent.Error -> {
                    val errorMessage = uiState.message
                    loading.value = false
                    error.value = errorMessage
                }
            }
            HomeContent(
                featured.value,
                popularProducts.value,
                categories.value,
                loading.value,
                error.value,
                onClick = { product ->
                    navController.navigate(ProductDetails(UiProductModel.fromProduct(product)))
                }
            )
        }
    }
}

@Composable
fun HomeContent(
    featured: List<Product>,
    popularProducts: List<Product>,
    categories: List<String>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onClick: (Product) -> Unit
) {
    LazyColumn {
        item {
            ProfileHeader()
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar(value = "", onTextChanged = {})
            Spacer(modifier = Modifier.size(16.dp))
        }
        item {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    Text("Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
            errorMessage?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            if (categories.isNotEmpty()) {
                HomeCategoriesRow(categories = categories, title = "Categories")
                Spacer(modifier = Modifier.size(16.dp))
            }
            if (featured.isNotEmpty()) {
                HomeProductRow(products = featured, title = "Featured", onClick = onClick)
                Spacer(modifier = Modifier.size(16.dp))
            }
            if (popularProducts.isNotEmpty()) {
                HomeProductRow(
                    products = popularProducts,
                    title = "Popular Products",
                    onClick = onClick
                )
            }
        }
    }
}