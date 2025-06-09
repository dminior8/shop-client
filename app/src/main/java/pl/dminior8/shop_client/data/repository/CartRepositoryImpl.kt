package pl.dminior8.shop_client.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.dminior8.shop_client.data.remote.dto.CartDto
import pl.dminior8.shop_client.data.remote.dto.CartItemDto
import pl.dminior8.shop_client.domain.repository.CartRepository
import pl.dminior8.shop_client.network.ShopApi
import java.util.UUID // Importuj UUID
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: ShopApi,
    private val userId: UUID // userId dostarczane do repozytorium (stałe dla sesji)
) : CartRepository{
        override fun getCartItems(): Flow<List<CartItemDto>> = flow {
        val cartDto = api.getCartItems(userId) // api.getCartItems() zwraca CartDto
        emit(cartDto.items) // Emitujemy listę CartItemDto z pola 'items' w CartDto
    }.flowOn(Dispatchers.IO)

    override suspend fun addToCart(productId: UUID, quantity: Int) {
        api.addToCart(userId, productId, quantity) // Wywołanie bezpośrednio z parametrami
        // Ponieważ api.addToCart zwraca Unit, nie ma sensu zwracać CartItemDto.
        // Jeśli potrzebujesz aktualnego koszyka po dodaniu, musisz ponownie wywołać getCartItems(userId)
    }

    override suspend fun removeFromCart(productId: UUID, quantity: Int) {
        api.removeFromCart(userId, productId, quantity) // Wywołanie bezpośrednio z parametrami
    }

    override suspend fun createCart() {
        api.createCart(userId)
    }

    override suspend fun checkoutCart() {
        api.checkoutCart(userId)
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