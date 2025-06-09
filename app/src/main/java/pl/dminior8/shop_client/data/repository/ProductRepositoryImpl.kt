package pl.dminior8.shop_client.data.repository


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import pl.dminior8.shop_client.data.entity.ProductEntity
import pl.dminior8.shop_client.data.local.ProductDao
import pl.dminior8.shop_client.data.remote.dto.ProductDto
import pl.dminior8.shop_client.domain.model.Product
import pl.dminior8.shop_client.domain.repository.ProductRepository
import pl.dminior8.shop_client.network.ShopApi
import java.util.UUID
import javax.inject.Inject

// ProductRepository - klasa odpowiedzialna za zarządzanie danymi produktów w aplikacji - łączy źródła danych:
// bazę (przez DAO) i zdalne API, dane udostępnia w postaci Flow

// @Inject - pozwala bibliotece DI automatycznie utworzyć istnację tej klasy i wstrzyknąć zależności

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val api: ShopApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ProductRepository {
    override fun getProductsFlow(): Flow<List<Product>> = dao.getAll()
        .map { entities -> entities.map { it.toDomain() } } // konwersja encji db na model domenowy (Product)
        .onStart { //wywoływanie przed emisją Flow
            emitAll(refreshAndGetLocal()) // odświeżenie danych z sieci i emisja zaktualizowanej listy z bazy
        }

    override fun getProductDetailFlow(id: UUID): Flow<Product> =
        dao.getById(id)
            .mapNotNull { it?.toDomain() } // dodatkowo pomija wartości null
            .onStart {
                val dto = api.getProductDetail(id)
                val entity = ProductEntity.fromDto(dto)
                dao.insertAll(listOf(entity))
                emit(entity.toDomain())
            }
            .flowOn(ioDispatcher) // w kontekście korutyn - komponent decydujący na jakim wątku/pulach zostanie wykonana korutyna
    // to część tzw. kontekstu korutyny

    private fun refreshAndGetLocal(): Flow<List<Product>> = flow {
        val remoteDtos: List<ProductDto> = api.getProducts()
        val entities: List<ProductEntity> = remoteDtos.map { ProductEntity.fromDto(it) }
        dao.clearAll() // czyszczenie lokalnej bazy
        dao.insertAll(entities) // i zapis zaktualizowanej listy produktów
        emit(dao.getAll().first().map { it.toDomain() }) // ręczna emisja wartości w Flow
    }.flowOn(ioDispatcher) // flow działa na dispatcherze IO - w tle, nie blokując głównego wątku

}