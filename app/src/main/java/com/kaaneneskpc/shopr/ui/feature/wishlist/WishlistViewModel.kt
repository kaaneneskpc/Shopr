package com.kaaneneskpc.shopr.ui.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.Wishlist
import com.kaaneneskpc.domain.model.WishlistItem
import com.kaaneneskpc.shopr.model.WishlistStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WishlistViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<WishlistUiState>(WishlistUiState.Loading)
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()

    private val _wishlistItemsState = MutableStateFlow<WishlistItemsUiState>(WishlistItemsUiState.Loading)
    val wishlistItemsState: StateFlow<WishlistItemsUiState> = _wishlistItemsState.asStateFlow()

    init {
        getWishlists()
        getWishlistItems()
    }

    fun getWishlists() {
        viewModelScope.launch {
            _uiState.value = WishlistUiState.Loading
            WishlistStore.wishlists.collectLatest { wishlists ->
                _uiState.value = WishlistUiState.Success(wishlists)
            }
        }
    }

    fun getWishlistItems() {
        viewModelScope.launch {
            _wishlistItemsState.value = WishlistItemsUiState.Loading
            WishlistStore.wishlistItems.collectLatest { items ->
                _wishlistItemsState.value = WishlistItemsUiState.Success(items)
            }
        }
    }

    fun removeFromWishlist(productId: Int) {
        WishlistStore.removeFromWishlist(productId)
    }
}

sealed class WishlistUiState {
    object Loading : WishlistUiState()
    data class Success(val wishlists: List<Wishlist>) : WishlistUiState()
    data class Error(val message: String) : WishlistUiState()
}

sealed class WishlistItemsUiState {
    object Loading : WishlistItemsUiState()
    data class Success(val items: List<WishlistItem>) : WishlistItemsUiState()
    data class Error(val message: String) : WishlistItemsUiState()
} 