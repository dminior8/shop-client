package pl.dminior8.shop_client.data.remote.dto;

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

// adnotacja biblioteki Moshi - do serializacji/deserializacj JSON
// generowanie automatyczne adapteru do konwersji, między JSON, a tym obiektem
@JsonClass(generateAdapter = true)
data class CartItemDto(
    val id: UUID,

    val cartId: UUID,

    @Json(name = "product_id") // mapuje productId do product_id
    val productId: UUID,

//    val name: String,

    val quantity: Int,

    val price: Double
)