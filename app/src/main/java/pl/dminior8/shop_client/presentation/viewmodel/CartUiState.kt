package pl.dminior8.shop_client.presentation.viewmodel

import pl.dminior8.shop_client.data.remote.dto.CartItemDto

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val items: List<CartItemDto>, val total: Double) : CartUiState()
    data class Error(val message: String) : CartUiState()
}