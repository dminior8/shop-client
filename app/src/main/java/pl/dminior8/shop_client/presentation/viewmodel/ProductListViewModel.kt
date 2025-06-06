package pl.dminior8.shop_client.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import pl.dminior8.shop_client.data.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init { loadProducts() }

    private fun loadProducts() {
        viewModelScope.launch {
            repo.getProductsFlow()
                .catch { e ->
                    Log.e("ProductListViewModel", "Error loading products", e)
                    _uiState.value = ProductListUiState.Error("Network error")
                }
                .collect { list -> _uiState.value = ProductListUiState.Success(list) }
        }
    }
}