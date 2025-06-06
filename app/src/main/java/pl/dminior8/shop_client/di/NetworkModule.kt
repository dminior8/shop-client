package pl.dminior8.shop_client.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.dminior8.shop_client.data.remote.moshi.UUIDAdapter
import pl.dminior8.shop_client.network.ShopApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL =
    "http://10.0.2.2:8080" // BFF running on localhost:8080 (Android emulator)

@Module // deklaruje klasę jako moduł dostarczający zależności dla Hilt
@InstallIn(SingletonComponent::class) // zakres życia zależności (jedna na całą aplikację)
object NetworkModule { // moduł musi być obiektem, bo Hilt wymaga dostępu do statycznych metod
    @Provides // dostarcza instancję danego typu
    @Singleton // tylko jedna instancja w całej aplikacji
    // dostarczanie Moshi
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // adapter dla Moshi do poprawnej obsługi klas Kotlina
        .add(UUIDAdapter())
        .build()

    @Provides
    @Singleton
    // konfiguracja OkHttpClient - klient HTTP używany przez Retrofit
    fun provideOkHttp(): OkHttpClient {
        // loguje pełne body requestów/response
        val logging = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) } // {} to funkcja zakresowa, skonfigurowanie obiektu tuż po jego stworzeniu
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    // tworzenie Retrofit i ShopApi
    fun provideShopApi(okHttp: OkHttpClient, moshi: Moshi): ShopApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ShopApi::class.java) // .java to przekazanie typu klasy lub metody z Javy (np. Retrofit napisany w Javie):
        // pod spodem konwersja z KClass - Kotlina na oczekiwane przez Javę Class<T>
    }
}