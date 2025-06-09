package pl.dminior8.shop_client.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import pl.dminior8.shop_client.data.local.ProductDao
import pl.dminior8.shop_client.data.local.getOrCreateUserId
import pl.dminior8.shop_client.data.repository.CartRepositoryImpl
import pl.dminior8.shop_client.data.repository.ProductRepositoryImpl
import pl.dminior8.shop_client.domain.repository.CartRepository
import pl.dminior8.shop_client.domain.repository.ProductRepository
import pl.dminior8.shop_client.network.ShopApi
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository( // argumenty funkcji automatycznie wstrzykiwane przez Hilt
        productDao: ProductDao,
        shopApi: ShopApi,
    ): ProductRepository = ProductRepositoryImpl(productDao, shopApi) // zwracana wartość: instancja z przekazanymi zależnościami

    @Provides
    @Singleton
    fun provideAnonymousUserId(@ApplicationContext context: Context): UUID {
        // RunBlocking bo Hilt potrzebuje synchronicznej wartości w momencie tworzenia grafu zależności
        return runBlocking {
            getOrCreateUserId(context)
        }
    }

    @Provides
    @Singleton
    fun provideCartRepository(shopApi: ShopApi, userId: UUID): CartRepository = CartRepositoryImpl(shopApi, userId)
}