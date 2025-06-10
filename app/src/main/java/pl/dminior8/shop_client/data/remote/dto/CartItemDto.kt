package pl.dminior8.shop_client.data.remote.dto;

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import pl.dminior8.shop_client.domain.model.CartItem
import java.util.*

// adnotacja biblioteki Moshi - do serializacji/deserializacj JSON
// generowanie automatyczne adapteru do konwersji, między JSON, a tym obiektem
@JsonClass(generateAdapter = true)
data class CartItemDto(
    val id: UUID,

    val cartId: UUID,

    val productId: UUID,

    @Json(name = "name")
    val productName: String,

    val quantity: Int,

    val price: Double
)

// Funkcja rozszerzająca do mapowania DTO na model domenowy
fun CartItemDto.toDomain(): CartItem {
    return CartItem(
        id = this.id,
        cartId = this.cartId,
        productId = this.productId,
        productName = this.productName,
        quantity = this.quantity,
        price = this.price
    )
}