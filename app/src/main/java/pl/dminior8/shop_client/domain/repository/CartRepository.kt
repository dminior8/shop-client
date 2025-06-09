package pl.dminior8.shop_client.domain.repository


import kotlinx.coroutines.flow.Flow
import pl.dminior8.shop_client.data.remote.dto.CartDto
import pl.dminior8.shop_client.data.remote.dto.CartItemDto
import java.util.UUID

interface CartRepository {
    fun getCartItems(): Flow<List<CartItemDto>>
    suspend fun addToCart(productId: UUID, quantity: Int)
    suspend fun removeFromCart(productId: UUID, quantity: Int)
    suspend fun createCart()
    suspend fun checkoutCart()
    fun getCartById(cartId: UUID): Flow<CartDto>
    fun getCartTotal(): Flow<Double>
    // fun getCartItemCount(): Flow<Int> // Opcjonalnie, jeśli chcesz ten licznik w interfejsie
}