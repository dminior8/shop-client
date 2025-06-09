package pl.dminior8.shop_client.network

import pl.dminior8.shop_client.data.remote.dto.CartDto
import pl.dminior8.shop_client.data.remote.dto.ProductDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ShopApi {
    @GET("/api/v1/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("/api/v1/products/{id}")
    suspend fun getProductDetail(@Path("id") id: UUID): ProductDto

    // Tworzenie (lub odświeżenie) koszyka dla użytkownika
    @POST("/api/v1/user/{userId}/cart")
    suspend fun createCart(@Path("userId") userId: UUID)

    // Dodawanie produktu do koszyka – użycie query params zamiast body
    @POST("/api/v1/user/{userId}/cart/add-product")
    suspend fun addToCart(
        @Path("userId") userId: UUID,
        @Query("productId") productId: UUID,
        @Query("quantity") quantity: Int
    )

    // Usuwanie produktu z koszyka – usunięcie określonej ilości
    @DELETE("/api/v1/user/{userId}/cart/remove-product")
    suspend fun removeFromCart(
        @Path("userId") userId: UUID,
        @Query("productId") productId: UUID,
        @Query("quantity") quantity: Int
    )

    // Finalizacja koszyka (checkout)
    @POST("/api/v1/user/{userId}/cart/checkout")
    suspend fun checkoutCart(@Path("userId") userId: UUID)

    // Pobranie zawartości koszyka
    @GET("/api/v1user/{userId}/cart")
    suspend fun getCartItems(@Path("userId") userId: UUID): CartDto

    @GET("/api/v1/user/{userId}/cart/{cartId}")
    suspend fun getCartById(
        @Path("userId") userId: UUID,
        @Path("cartId") cartId: UUID
    ): CartDto

    // Pobranie sumy wartości koszyka
    @GET("/api/v1/user/{userId}/cart/total")
    suspend fun getCartTotal(@Path("userId") userId: UUID): Double

}