package com.kaaneneskpc.shopr.ui.feature.allproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AllProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllProductsScreenEvent>(AllProductsScreenEvent.Loading)
    val uiState: StateFlow<AllProductsScreenEvent> = _uiState

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _uiState.value = AllProductsScreenEvent.Loading
            try {
                val featuredProducts = getProducts(1)
                val popularProducts = getProducts(2)
                
                val allProducts = (featuredProducts + popularProducts).distinctBy { it.id }
                
                _uiState.value = AllProductsScreenEvent.Success(allProducts)
            } catch (e: Exception) {
                _uiState.value = AllProductsScreenEvent.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    private suspend fun getProducts(category: Int?): List<Product> {
        getProductsUseCase.execute(category).let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    return result.value.products
                }
                is ResultWrapper.Failure -> {
                    return emptyList()
                }
            }
        }
    }
}

sealed class AllProductsScreenEvent {
    object Loading : AllProductsScreenEvent()
    data class Success(val products: List<Product>) : AllProductsScreenEvent()
    data class Error(val message: String) : AllProductsScreenEvent()
} 