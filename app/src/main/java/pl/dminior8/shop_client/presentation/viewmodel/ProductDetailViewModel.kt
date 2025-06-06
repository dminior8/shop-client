package pl.dminior8.shop_client.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.dminior8.shop_client.data.repository.CartRepository
import pl.dminior8.shop_client.data.repository.ProductRepository
import pl.dminior8.shop_client.domain.model.Product
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: UUID = UUID.fromString(savedStateHandle["userId"] ?: error("Brak userId"))

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _addedToCart = MutableSharedFlow<Boolean>()
    val addedToCart: SharedFlow<Boolean> = _addedToCart.asSharedFlow()

    fun loadProduct(id: UUID) {
        viewModelScope.launch {
            productRepo.getProductDetailFlow(id)
                .catch { _uiState.value = ProductDetailUiState.Error("Failed to load the product") }
                .collect { prod -> _uiState.value = ProductDetailUiState.Success(prod) }
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                cartRepo.addToCart(userId, product.id, quantity)
                _addedToCart.emit(true)
            } catch (e: Exception) {
                _addedToCart.emit(false)
                Log.e("ProductDetailViewModel", "Error during adding product to cart", e)
            }
        }
    }
}