package pl.dminior8.shop_client.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import pl.dminior8.shop_client.domain.model.Product
import java.util.*

// DTO received from Retrofit
@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: UUID,

    val name: String,

    val description: String?,

    val price: Double,

    @Json(name = "availableQuantity")
    val availableQty: Int,

) {
    fun toDomain(): Product = Product(id, name, description, price, availableQty)
}