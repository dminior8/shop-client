package pl.dminior8.shop_client.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.dminior8.shop_client.data.entity.ProductEntity

// deklaracja klasy jako bazy danych Room
// entities - lista encji (tabel), które będą w tej bazie
// version - wersja bazy danych (przydatne przy migracjach)
@Database(entities = [ProductEntity::class], version = 1)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao // udostępnia DAO (interfejs do operacji na bazie) dla produktów
}