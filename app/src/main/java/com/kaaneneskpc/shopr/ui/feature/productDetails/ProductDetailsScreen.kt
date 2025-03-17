package com.kaaneneskpc.shopr.ui.feature.productDetails

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaaneneskpc.shopr.R
import com.kaaneneskpc.shopr.model.UiProductModel
import com.kaaneneskpc.shopr.model.WishlistStore
import com.kaaneneskpc.shopr.ui.feature.productDetails.components.SizeItem
import com.kaaneneskpc.shopr.ui.theme.Blue
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProductDetailsScreen(
    navController: NavController,
    product: UiProductModel,
    viewModel: ProductDetailsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    var isInWishlist by remember { mutableStateOf(WishlistStore.isInWishlist(product.id)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(350.dp)
            )
            
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Geri",
                    tint = Color.Black
                )
            }
            
            IconButton(
                onClick = { 
                    if (isInWishlist) {
                        WishlistStore.removeFromWishlist(product.id)
                        Toast.makeText(context, "Product selected from your wishlistProduct removed from your wishlist", Toast.LENGTH_SHORT).show()
                    } else {
                        WishlistStore.addToWishlist(
                            productId = product.id,
                            productTitle = product.title,
                            productPrice = product.price,
                            productImageUrl = product.image
                        )
                        Toast.makeText(context, "Product added to your wish list", Toast.LENGTH_SHORT).show()
                    }
                    isInWishlist = !isInWishlist
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isInWishlist) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isInWishlist) "Remove from My Wishlist" else "Add to Wishlist",
                    tint = if (isInWishlist) Color.Red else Color.Black
                )
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                Text(
                    text = "${product.price}$",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    color = Blue
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.ic_star), contentDescription = null)
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "4.5",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "(10 Reviews)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Size",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                repeat(4) {
                    SizeItem(size = "${it + 1}", isSelected = it == 0) {}
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = { viewModel.addProductToCart(product) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Buy Now")
                }
                Spacer(modifier = Modifier.size(8.dp))
                IconButton(
                    onClick = { viewModel.addProductToCart(product) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = IconButtonDefaults.iconButtonColors()
                        .copy(containerColor = Color.LightGray.copy(alpha = 0.4f))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = null
                    )
                }
            }

        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val uiState = viewModel.state.collectAsStateWithLifecycle()
        val loading = remember {
            mutableStateOf(false)
        }
        LaunchedEffect(uiState.value) {
            when (uiState.value) {
                is ProductDetailsEvent.Loading -> {
                    loading.value = true
                }

                is ProductDetailsEvent.Success -> {
                    loading.value = false
                    Toast.makeText(
                        navController.context,
                        (uiState.value as ProductDetailsEvent.Success).message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ProductDetailsEvent.Error -> {
                    Toast.makeText(
                        navController.context,
                        (uiState.value as ProductDetailsEvent.Error).message,
                        Toast.LENGTH_LONG
                    ).show()
                    loading.value = false
                }

                else -> {
                    loading.value = false
                }
            }
        }

        if (loading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize().clickable(enabled = false) {},
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

