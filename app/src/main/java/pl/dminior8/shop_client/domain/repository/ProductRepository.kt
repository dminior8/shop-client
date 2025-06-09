package pl.dminior8.shop_client.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.dminior8.shop_client.domain.model.Product
import java.util.UUID

interface ProductRepository {
    /**
     * Zapewnia strumień (Flow) listy produktów.
     * Na początku próbuje pobrać produkty z lokalnej bazy danych.
     * Następnie odświeża dane z sieci, aktualizuje lokalną bazę i emituje zaktualizowaną listę.
     */
    fun getProductsFlow(): Flow<List<Product>>

    /**
     * Zapewnia strumień (Flow) szczegółów pojedynczego produktu.
     * Próbuje pobrać szczegóły z lokalnej bazy danych.
     * Jeśli produkt nie jest obecny lokalnie lub dane wymagają odświeżenia,
     * pobiera szczegóły z sieci, aktualizuje lokalną bazę i emituje zaktualizowane dane.
     *
     * @param id UUID produktu, którego szczegóły mają zostać pobrane.
     */
    fun getProductDetailFlow(id: UUID): Flow<Product>
}