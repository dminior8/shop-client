package pl.dminior8.shop_client.domain.model


import java.util.*

// Domain Model - reprezentuje produkt w logice biznesowej aplikacji
// nie zawiera pól technicznych (np. createdAt), skupia się na tym co istotne dla biznesu i prezentacji
data class Product(
    val id: UUID,
    val name: String,
    val description: String?,
    val price: Double,
    val availableQty: Int
)