package pl.dminior8.shop_client.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.dminior8.shop_client.data.local.ProductDao
import pl.dminior8.shop_client.data.repository.CartRepository
import pl.dminior8.shop_client.data.repository.ProductRepository
import pl.dminior8.shop_client.network.ShopApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository( // argumenty funkcji automatycznie wstrzykiwane przez Hilt
        productDao: ProductDao,
        shopApi: ShopApi
    ): ProductRepository = ProductRepository(productDao, shopApi) // zwracana wartość: instancja z przekazanymi zależnościami

    @Provides
    @Singleton
    fun provideCartRepository(shopApi: ShopApi): CartRepository = CartRepository(shopApi)
}