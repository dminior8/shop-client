package pl.dminior8.shop_client.presentation.ui


import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.dminior8.shop_client.data.local.getOrCreateUserId
import java.util.UUID

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var userId by remember { mutableStateOf<UUID?>(null) }

    LaunchedEffect(Unit) {
        userId = getOrCreateUserId(context)
    }

    if (userId != null) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "product_list") {
            composable("product_list") {
                ProductListScreen(onProductClick = { id ->
                    navController.navigate("product_detail/$id")
                }, onCartClick = {
                    navController.navigate("cart/${userId}")
                })
            }
            composable("product_detail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(productId = UUID.fromString(productId), onCartClick = {
                    navController.navigate("cart/${userId}")
                })
            }
            composable("cart/{userId}") { backStackEntry ->
                val userIdString = backStackEntry.arguments?.getString("userId") ?: ""
                val userId = UUID.fromString(userIdString)
                CartScreen(userId = userId)
            }
        }
    } else {
        // Możesz pokazać SplashScreen lub CircularProgressIndicator
    }
}