package com.kaaneneskpc.shopr.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val getProductsUseCase: GetProductsUseCase): ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenEvent>(HomeScreenEvent.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenEvent.Loading
            val featuredProducts = getProducts("electronics")
            val popularProducts = getProducts("jewelery")
            if(featuredProducts.isEmpty() || popularProducts.isEmpty()) {
                _uiState.value = HomeScreenEvent.Error("Error fetching products")
                return@launch
            }
            _uiState.value = HomeScreenEvent.Success(featuredProducts, popularProducts)
        }
    }

    private suspend fun getProducts(category: String?): List<Product> {
        getProductsUseCase.execute(category).let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    return result.value
                }

                is ResultWrapper.Failure -> {
                    return emptyList()
                }
            }
        }
    }
}


sealed class HomeScreenEvent {
    data object Loading: HomeScreenEvent()
    data class Success(val featured: List<Product>, val popularProducts: List<Product>): HomeScreenEvent()
    data class Error(val message: String): HomeScreenEvent()
}