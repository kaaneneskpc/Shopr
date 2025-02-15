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
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenEvent.Loading
            getProductsUseCase.execute().let { result ->
                when(result) {
                    is ResultWrapper.Success -> {
                        _uiState.value = HomeScreenEvent.Success(result.value)
                    }
                    is ResultWrapper.Failure -> {
                        _uiState.value = HomeScreenEvent.Error(result.exception.message ?: "An error occurred")
                    }
                }
            }
        }
    }
}


sealed class HomeScreenEvent {
    data object Loading: HomeScreenEvent()
    data class Success(val data: List<Product>): HomeScreenEvent()
    data class Error(val message: String): HomeScreenEvent()
}