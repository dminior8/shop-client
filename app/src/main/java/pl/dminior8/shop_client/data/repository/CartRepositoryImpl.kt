package pl.dminior8.shop_client.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import pl.dminior8.shop_client.data.remote.dto.CartDto
import pl.dminior8.shop_client.data.remote.dto.toDomain
import pl.dminior8.shop_client.domain.model.CartItem
import pl.dminior8.shop_client.domain.repository.CartRepository
import pl.dminior8.shop_client.network.ShopApi
import java.util.UUID
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: ShopApi,
    private val userId: UUID // userId dostarczane do repozytorium (stałe dla sesji)
) : CartRepository{
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getCartItems(): Flow<List<CartItem>> = _cartItems.asStateFlow()

    private suspend fun fetchAndPublishCurrentCart() {
        withContext(Dispatchers.IO) { // Wykonuj operacje sieciowe na dispatcherze IO
            try {
                val cartDto = api.getCartItems(userId)
                // Maping z CartItemDto na CartItem (model domenowy) przed zapisaniem do Flow
                _cartItems.value = cartDto.items.map { it.toDomain() }
            } catch (e: Exception) {
                throw e
            }
        }
    }

//    override fun getCartItems(): Flow<List<CartItemDto>> = flow {
//        val cartDto = api.getCartItems(userId) // api.getCartItems() zwraca CartDto
//        emit(cartDto.items) // Emitujemy listę CartItemDto z pola 'items' w CartDto
//    }.flowOn(Dispatchers.IO)

    override suspend fun addToCart(productId: UUID, quantity: Int) {
        api.addToCart(userId, productId, quantity) // Wywołanie bezpośrednio z parametrami
        fetchAndPublishCurrentCart()
    }

    override suspend fun removeFromCart(productId: UUID, quantity: Int) {
        api.removeFromCart(userId, productId, quantity) // Wywołanie bezpośrednio z parametrami
        fetchAndPublishCurrentCart()
    }

    override suspend fun createCart() {
        api.createCart(userId)
    }

    override suspend fun checkoutCart() {
        api.checkoutCart(userId)
        fetchAndPublishCurrentCart()
    }

    override fun getCartById(cartId: UUID): Flow<CartDto> = flow {
        val cartDto = api.getCartById(userId, cartId)
        emit(cartDto)
    }.flowOn(Dispatchers.IO)

    override fun getCartTotal(): Flow<Double> = flow {
        val total = api.getCartTotal(userId)
        emit(total)
    }.flowOn(Dispatchers.IO)
}