package pl.dminior8.shop_client.presentation.viewmodel


import pl.dminior8.shop_client.domain.model.Product

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}