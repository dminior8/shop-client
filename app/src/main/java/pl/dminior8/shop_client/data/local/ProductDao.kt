package pl.dminior8.shop_client.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.dminior8.shop_client.data.entity.ProductEntity
import java.util.*

@Dao // adnotacja bibliotek Room; Room  połączy się z bazą zdefiniowaną w klasie @Database
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getById(id: UUID): Flow<ProductEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // jeśli już istnieje (ten sam klucz), będzie zastąpiony
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearAll()
}
