package pl.dminior8.shop_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import pl.dminior8.shop_client.presentation.theme.ShopclientTheme
import pl.dminior8.shop_client.presentation.ui.MainScreen
import pl.dminior8.shop_client.util.SyncProductsWorker

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SyncProductsWorker.schedule(applicationContext) // nie ma static
        setContent {
            ShopclientTheme {
                MainScreen()
            }
        }
    }
}
