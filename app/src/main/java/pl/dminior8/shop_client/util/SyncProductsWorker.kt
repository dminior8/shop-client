package pl.dminior8.shop_client.util

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import pl.dminior8.shop_client.data.repository.ProductRepository
import java.util.concurrent.TimeUnit

// adnotacja @AssistedInject i @Assisted:
// gdy część zależności musi być przekazywana przez WorkManagera, a część przez DI (productRepository)

// : CoroutineWorker(context, params) - dziedziczenie po workerze (do asynch operacji w tle) pozwalającym używać:
// "zawieszanych funkcji" (suspend) - pozwalają na zawieszanie działania w dowolnym miejscu i wznowienie później
// (bez blokowania wątku na którym działa);
// można je wywoływać z innych funkcji suspend, albo z wnętrza korutyny
// korutyn - lekki mechanizm współbieżności (wiele zadań równolegle lub asynchronicznie); może być zawieszona;
// zarządzane przez środowisko wykonawcze Kotlina

// suspend - metoda zawieszana, może korzystać z korutyn
// Result = coroutineScope - zapewnia, że korutyny w tym scope muszą się zakończyć przed zakończeniem pracy workera

// Flow emituje sekwencję wartości asynchronicznie; reaguje automatycznie na nowe zmiany (emituje zawsze aktualne dane)
// (jest to rodzaj reaktywnego strumienia danych); collect uruchamia kod w środku flow

// companion object - wszystko co w tym obiekcie jest deklarowane podobnie jak static

@HiltWorker //wstrzykiwanie zależności do workera przez Hilt
class SyncProductsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val productRepository: ProductRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            productRepository.getProductsFlow()
                .collect {} // uruchamia Flow i czeka na jego zakończenie
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncProductsWorker", "Flow error", e)
            Result.retry() // próba ponownego uruchomienia zadania
        }
    }

    companion object {
        fun schedule(context: Context) { // funkcja do planowania cyklicznego uruchamiania workera przez WorkManagera
            val constraints = Constraints.Builder() //warunki do spełnienia
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<SyncProductsWorker>(
                1,
                TimeUnit.HOURS
            ) // zlecenie cykliczne
                .setConstraints(constraints)
                .build()

            // dodaje cykliczne zadanie, które nie będzie powielane, jeśli już istnieje
            WorkManager.Companion.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
                "sync_products",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}