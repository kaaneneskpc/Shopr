package com.kaaneneskpc.shopr.ui.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaaneneskpc.domain.model.Product
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.kaaneneskpc.shopr.navigation.AllProductsScreen
import com.kaaneneskpc.shopr.ui.theme.Blue

@Composable
fun HomeProductRow(
    products: List<Product>, 
    title: String, 
    onClick: (Product) -> Unit,
    navController: NavController? = null
) {
    val isVisible = remember { mutableStateOf(false) }
    val isProductSection = title == "Featured Products" || title == "Popular Products"
    
    Column {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(
                    Alignment.CenterStart
                ),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color = Blue,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(enabled = isProductSection && navController != null) {
                        if (isProductSection && navController != null) {
                            navController.navigate(AllProductsScreen)
                        }
                    }
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow {
            items(products, key = { it.id }) { product ->
                LaunchedEffect(true) { isVisible.value = true }
                AnimatedVisibility(
                    visible = isVisible.value,
                    enter = fadeIn() + expandVertically()
                ) {
                    ProductItem(product = product, onClick = onClick)
                }
            }
        }
    }
}