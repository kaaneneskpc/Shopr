package com.kaaneneskpc.shopr.ui.feature.home.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.domain.usecase.GetCartUseCase
import com.kaaneneskpc.shopr.model.WishlistStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@SuppressLint("DefaultLocale")
@Composable
fun ProductItem(product: Product, onClick: (Product) -> Unit) {
    val context = LocalContext.current
    var isInWishlist by remember { mutableStateOf(WishlistStore.isInWishlist(product.id)) }
    val getCartUseCase: GetCartUseCase by inject(GetCartUseCase::class.java)
    val coroutineScope = rememberCoroutineScope()
    
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(160.dp)
            .height(250.dp)
            .clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        if (isInWishlist) {
                            WishlistStore.removeFromWishlist(product.id)
                            Toast.makeText(context, "Product removed from your wishlist", Toast.LENGTH_SHORT).show()
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
                        contentDescription = if (isInWishlist) "Remove from My Wishlist" else "Add to My Wishlist",
                        tint = if (isInWishlist) Color.Red else Color.Black
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    FilledTonalIconButton(
                        onClick = { 
                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    delay(500)
                                    getCartUseCase.execute()
                                    launch(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "${product.title} added to cart",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    launch(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "There was an error adding the product to the cart: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}