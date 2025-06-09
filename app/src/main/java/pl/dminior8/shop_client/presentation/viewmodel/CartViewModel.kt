package pl.dminior8.shop_client.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.dminior8.shop_client.domain.repository.CartRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init { fetchCart() }

    fun fetchCart() {
        viewModelScope.launch {
            cartRepository.getCartItems()
                .catch { e ->
                    _uiState.value = CartUiState.Error("Cart fetching error")
                    Log.e("CartViewModel", "Error during removing product from cart", e)
                }
                .collect { items ->
                    val total = items.sumOf { it.price * it.quantity }
                    _uiState.value = CartUiState.Success(items, total)
                }
        }
    }

    fun removeFromCart(productId: UUID, quantity: Int) {
        viewModelScope.launch {
            try {
                cartRepository.removeFromCart(
                    productId = productId,
                    quantity = quantity
                )
                // Immediately fetch updated cart state
                fetchCart()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error during remove product from cart")
                Log.e("CartViewModel", "Error during removing product from cart", e)
            }
        }
    }
}