package pl.dminior8.shop_client.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.dminior8.shop_client.data.local.AppDatabase
import pl.dminior8.shop_client.data.local.ProductDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "shop-db").build() // tworzy instancję bazy danych Room
    }

    @Provides
    // zwracanie DAO - skrócona składnia =, zamiast return; brak Singletona - tworzenie na bieżąco, bo DAO "tanie" w tworzeniu
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
}