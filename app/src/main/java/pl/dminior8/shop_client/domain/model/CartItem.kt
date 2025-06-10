package pl.dminior8.shop_client.domain.model


import java.util.UUID

data class CartItem(
    val id: UUID,
    val cartId: UUID,
    val productId: UUID,
    val productName: String,
    val quantity: Int,
    val price: Double
)