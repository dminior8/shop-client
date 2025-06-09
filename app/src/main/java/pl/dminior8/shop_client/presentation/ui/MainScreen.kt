package pl.dminior8.shop_client.presentation.ui


import androidx.compose.material3.CircularProgressIndicator
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
                ProductListScreen(onProductClick = { productId ->
                    navController.navigate("product_detail/${userId}/${productId}")
                }, onCartClick = {
                    navController.navigate("cart/${userId}")
                })
            }
            composable("product_detail/{userId}/{productId}") { backStackEntry ->
                val userId = UUID.fromString(backStackEntry.arguments?.getString("userId") ?: "")
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    productId = UUID.fromString(productId), onCartClick = {
                    navController.navigate("cart/${userId}")
                })
            }
            composable("cart/{userId}") { backStackEntry ->
                val userId = UUID.fromString(backStackEntry.arguments?.getString("userId") ?: "")
                CartScreen(userId = userId)
            }
        }
    } else {
        CircularProgressIndicator()
    }
}