package com.kaaneneskpc.shopr.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.usecase.GetCategoriesUseCase
import com.kaaneneskpc.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val getProductsUseCase: GetProductsUseCase, private val getCategoriesUseCase: GetCategoriesUseCase): ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenEvent>(HomeScreenEvent.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenEvent.Loading
            val featuredProducts = getProducts(1)
            val popularProducts = getProducts(2)
            val categories = getCategory()
            if(featuredProducts.isEmpty() && popularProducts.isEmpty() && categories.isNotEmpty()) {
                _uiState.value = HomeScreenEvent.Error("Error fetching products")
                return@launch
            }
            _uiState.value = HomeScreenEvent.Success(featuredProducts, popularProducts, categories)
        }
    }

    private suspend fun getCategory(): List<String> {
        getCategoriesUseCase.execute().let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    return result.value.category.map { it.title }
                }

                is ResultWrapper.Failure -> {
                    return emptyList()
                }
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


sealed class HomeScreenEvent {
    data object Loading: HomeScreenEvent()
    data class Success(val featured: List<Product>, val popularProducts: List<Product>, val categories: List<String>): HomeScreenEvent()
    data class Error(val message: String): HomeScreenEvent()
}