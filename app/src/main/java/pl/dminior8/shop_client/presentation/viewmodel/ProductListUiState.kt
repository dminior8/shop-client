package pl.dminior8.shop_client.presentation.viewmodel


import pl.dminior8.shop_client.domain.model.Product

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success(val products: List<Product>) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}