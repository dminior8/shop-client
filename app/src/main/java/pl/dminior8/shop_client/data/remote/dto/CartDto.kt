package pl.dminior8.shop_client.data.remote.dto

import java.time.Instant
import java.util.UUID

data class CartDto(
    val id: UUID,
    val userId: UUID,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val items: List<CartItemDto> = emptyList() // domyślnie pusta lista
)