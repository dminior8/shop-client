package pl.dminior8.shop_client.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.dminior8.shop_client.data.remote.dto.CartDto
import pl.dminior8.shop_client.data.remote.dto.CartItemDto
import pl.dminior8.shop_client.network.ShopApi
import java.util.UUID // Importuj UUID
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api: ShopApi
) {
    // Zakładamy, że userId będzie dostarczane do repozytorium (np. przez ViewModel lub inną warstwę)
    // Możesz to zrobić na kilka sposobów:
    // 1. Przekazując userId do każdej metody (jak poniżej)
    // 2. Przekazując userId do konstruktora CartRepository, jeśli jest stałe dla sesji
    //    np. class CartRepository @Inject constructor(private val api: ShopApi, private val userId: UUID)

    fun getCartItems(userId: UUID): Flow<List<CartItemDto>> = flow {
        val cartDto = api.getCartItems(userId) // api.getCartItems() zwraca CartDto
        emit(cartDto.items) // Emitujemy listę CartItemDto z pola 'items' w CartDto
    }.flowOn(Dispatchers.IO)

    suspend fun addToCart(userId: UUID, productId: UUID, quantity: Int) {
        api.addToCart(userId, productId, quantity) // Wywołanie bezpośrednio z parametrami
        // Ponieważ api.addToCart zwraca Unit, nie ma sensu zwracać CartItemDto.
        // Jeśli potrzebujesz aktualnego koszyka po dodaniu, musisz ponownie wywołać getCartItems(userId)
    }

    suspend fun removeFromCart(userId: UUID, productId: UUID, quantity: Int) {
        api.removeFromCart(userId, productId, quantity) // Wywołanie bezpośrednio z parametrami
    }

    suspend fun createCart(userId: UUID) {
        api.createCart(userId)
    }

    suspend fun checkoutCart(userId: UUID) {
        api.checkoutCart(userId)
    }

    fun getCartById(userId: UUID, cartId: UUID): Flow<CartDto> = flow {
        val cartDto = api.getCartById(userId, cartId)
        emit(cartDto)
    }.flowOn(Dispatchers.IO)

    fun getCartTotal(userId: UUID): Flow<Double> = flow {
        val total = api.getCartTotal(userId)
        emit(total)
    }.flowOn(Dispatchers.IO)
}