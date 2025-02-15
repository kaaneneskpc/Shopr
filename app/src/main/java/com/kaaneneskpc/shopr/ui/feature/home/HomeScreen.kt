package com.kaaneneskpc.shopr.ui.feature.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.ui.feature.home.components.ProductItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context: Context = LocalContext.current

    when(uiState) {
        is HomeScreenEvent.Loading -> {
            CircularProgressIndicator()
        }
        is HomeScreenEvent.Success -> {
            val data = uiState.data
            LazyColumn {
                items(data) { product ->
                    ProductItem(product = product) {
                        Toast.makeText(context, "Product clicked: ${it.title}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        is HomeScreenEvent.Error -> {
            Text(text = uiState.message)
        }
    }

}