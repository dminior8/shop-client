package pl.dminior8.shop_client.data.remote.dto

import java.util.UUID

data class CartDto(
    val id: UUID,
    val userId: UUID,
    val items: List<CartItemDto> = emptyList() // domyślnie pusta lista
)