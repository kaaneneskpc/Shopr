package com.kaaneneskpc.shopr.ui.feature.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.shopr.ui.feature.home.components.HomeProductRow
import com.kaaneneskpc.shopr.ui.feature.home.components.ProfileHeader
import com.kaaneneskpc.shopr.ui.feature.home.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold {
        Surface(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            when (uiState) {
                is HomeScreenEvent.Loading -> {
                    CircularProgressIndicator()
                }

                is HomeScreenEvent.Success -> {
                    HomeContent(uiState.featured, uiState.popularProducts)
                }

                is HomeScreenEvent.Error -> {
                    Text(text = uiState.message)
                }
            }

        }
    }
}

@Composable
fun HomeContent(featured: List<Product>, popularProducts: List<Product>) {
    LazyColumn {
        item {
            ProfileHeader()
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar(value = "", onTextChanged = {})
            Spacer(modifier = Modifier.size(16.dp))
        }
        item {
            if (featured.isNotEmpty()) {
                HomeProductRow(products = featured, title = "Featured")
                Spacer(modifier = Modifier.size(16.dp))
            }
            if (popularProducts.isNotEmpty()) {
                HomeProductRow(products = popularProducts, title = "Popular Products")
            }
        }
    }
}