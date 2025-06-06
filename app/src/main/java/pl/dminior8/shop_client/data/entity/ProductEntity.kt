package pl.dminior8.shop_client.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.dminior8.shop_client.data.remote.dto.ProductDto
import pl.dminior8.shop_client.domain.model.Product
import java.util.UUID

// Room Entity for Product - bardziej technoczne, to co w bazie, metody pomocnicze, konwersje, używany przez DAO/Room
@Entity(tableName = "products") // tabela w lokalnej Room
data class ProductEntity(
    @PrimaryKey val id: UUID,
    val name: String,
    val description: String?,
    val price: Double,
    val availableQty: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    //metoda konwertująca encję bazy na model domenowy
    fun toDomain(): Product = Product(id, name, description, price, availableQty)

    companion object {
        fun fromDto(dto: ProductDto): ProductEntity = ProductEntity(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            price = dto.price,
            availableQty = dto.availableQty
//            createdAt = dto.createdAt ?: System.currentTimeMillis(),
//            updatedAt = dto.updatedAt ?: System.currentTimeMillis()
        )
    }
}