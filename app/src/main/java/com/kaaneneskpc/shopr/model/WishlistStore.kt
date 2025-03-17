package com.kaaneneskpc.shopr.model

import android.content.Context
import android.content.SharedPreferences
import com.kaaneneskpc.domain.model.Wishlist
import com.kaaneneskpc.domain.model.WishlistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Yerel wishlist veri deposu
 * Gerçek bir API yerine uygulama içinde wishlist verilerini saklar
 */
object WishlistStore {
    private val _wishlistItems = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItem>> = _wishlistItems.asStateFlow()
    
    private var nextItemId = 1
    
    // Varsayılan wishlist
    private val defaultWishlist = Wishlist(
        id = 1,
        userId = 1,
        name = "İstek Listem",
        isPublic = false,
        shareableLink = null,
        items = emptyList(),
        createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    )
    
    private val _wishlists = MutableStateFlow(listOf(defaultWishlist))
    val wishlists: StateFlow<List<Wishlist>> = _wishlists.asStateFlow()
    
    private lateinit var sharedPreferences: SharedPreferences
    private val json = Json { ignoreUnknownKeys = true }
    
    private const val PREF_NAME = "wishlist_preferences"
    private const val KEY_WISHLIST_ITEMS = "wishlist_items"
    private const val KEY_NEXT_ITEM_ID = "next_item_id"
    
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadFromPreferences()
    }
    
    private fun loadFromPreferences() {
        val itemsJson = sharedPreferences.getString(KEY_WISHLIST_ITEMS, null)
        if (itemsJson != null) {
            try {
                val items = json.decodeFromString<List<WishlistItemSerializable>>(itemsJson)
                    .map { it.toWishlistItem() }
                _wishlistItems.value = items
                updateWishlistItems()
            } catch (e: Exception) {
                // Handle parsing error
                _wishlistItems.value = emptyList()
            }
        }
        
        nextItemId = sharedPreferences.getInt(KEY_NEXT_ITEM_ID, 1)
    }
    
    private fun saveToPreferences() {
        val itemsJson = json.encodeToString(
            _wishlistItems.value.map { WishlistItemSerializable.fromWishlistItem(it) }
        )
        sharedPreferences.edit()
            .putString(KEY_WISHLIST_ITEMS, itemsJson)
            .putInt(KEY_NEXT_ITEM_ID, nextItemId)
            .apply()
    }
    
    fun addToWishlist(productId: Int, productTitle: String, productPrice: Double, productImageUrl: String? = null) {
        val existingItem = _wishlistItems.value.find { it.productId == productId }
        if (existingItem == null) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val newItem = WishlistItem(
                id = nextItemId++,
                productId = productId,
                dateAdded = currentDate,
                priceAtTimeOfAdding = productPrice,
                currentPrice = productPrice,
                isInStock = true,
                notifyOnPriceChange = false,
                productTitle = productTitle,
                productImageUrl = productImageUrl
            )
            
            _wishlistItems.update { currentItems ->
                currentItems + newItem
            }
            
            updateWishlistItems()
            saveToPreferences()
        }
    }
    
    fun removeFromWishlist(productId: Int) {
        _wishlistItems.update { currentItems ->
            currentItems.filter { it.productId != productId }
        }
        
        updateWishlistItems()
        saveToPreferences()
    }
    
    fun isInWishlist(productId: Int): Boolean {
        return _wishlistItems.value.any { it.productId == productId }
    }
    
    private fun updateWishlistItems() {
        _wishlists.update { currentWishlists ->
            currentWishlists.map { wishlist ->
                if (wishlist.id == defaultWishlist.id) {
                    wishlist.copy(items = _wishlistItems.value)
                } else {
                    wishlist
                }
            }
        }
    }
    
    fun clearWishlist() {
        _wishlistItems.value = emptyList()
        updateWishlistItems()
        saveToPreferences()
    }
}

// Serializable version of WishlistItem for JSON serialization
@kotlinx.serialization.Serializable
private data class WishlistItemSerializable(
    val id: Int,
    val productId: Int,
    val dateAdded: String,
    val priceAtTimeOfAdding: Double,
    val currentPrice: Double,
    val isInStock: Boolean,
    val notifyOnPriceChange: Boolean,
    val productTitle: String = "",
    val productImageUrl: String? = null
) {
    fun toWishlistItem(): WishlistItem {
        return WishlistItem(
            id = id,
            productId = productId,
            dateAdded = dateAdded,
            priceAtTimeOfAdding = priceAtTimeOfAdding,
            currentPrice = currentPrice,
            isInStock = isInStock,
            notifyOnPriceChange = notifyOnPriceChange,
            productTitle = productTitle,
            productImageUrl = productImageUrl
        )
    }
    
    companion object {
        fun fromWishlistItem(item: WishlistItem): WishlistItemSerializable {
            return WishlistItemSerializable(
                id = item.id,
                productId = item.productId,
                dateAdded = item.dateAdded,
                priceAtTimeOfAdding = item.priceAtTimeOfAdding,
                currentPrice = item.currentPrice,
                isInStock = item.isInStock,
                notifyOnPriceChange = item.notifyOnPriceChange,
                productTitle = item.productTitle,
                productImageUrl = item.productImageUrl
            )
        }
    }
} 