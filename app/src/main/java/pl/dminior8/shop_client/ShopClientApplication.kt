package pl.dminior8.shop_client

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.work.HiltWorkerFactory
import javax.inject.Inject

//Hilt ma przygotować DI dla całej aplikacji

//interfejsy; Application() - dziedzicząc startuje razem z aplikacją,
// Configuration.Provider - dostarcza konfigurację dla WorkManagera (komponent do wykonywania zadań
// w tle - nawet jeśli urządzenie zresetowane lub aplikacja zamknięta;
// rekomendowany do tzw. persistent work - praca która musi być wykonywana
// nawet jeśli przerwy w działaniu)

@HiltAndroidApp
class ShopClientApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
    }

    // Nadpisywanie właściwości (properties)
    override val workManagerConfiguration: Configuration //val - odpowiednik final
        get() = Configuration.Builder() // skrócony getter, tzw. expression body
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

}