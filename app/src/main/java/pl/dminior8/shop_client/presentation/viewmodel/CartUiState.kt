package pl.dminior8.shop_client.presentation.viewmodel

import pl.dminior8.shop_client.domain.model.CartItem

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val items: List<CartItem>, val total: Double) : CartUiState()
    data class Error(val message: String) : CartUiState()
}