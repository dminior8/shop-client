package pl.dminior8.shop_client.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.dminior8.shop_client.presentation.viewmodel.CartUiState
import pl.dminior8.shop_client.presentation.viewmodel.CartViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    userId: UUID, viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.fetchCart() } //automatyczne załadowanie koszyka po pierwszym załadowaniu
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cart") })
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            when (state) {
                CartUiState.Loading -> { // spinner
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CartUiState.Success -> { // lista produktów w koszyku i suma
                    val items = (state as CartUiState.Success).items
                    val total = (state as CartUiState.Success).total
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (items.isEmpty()) {
                            Text(
                                text = "Your cart is empty",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(items) { item ->
                                    CartListItem(
                                        item = item,
                                        onRemove = {
                                            // Przekazujemy productId I quantity z itemu
                                            viewModel.removeFromCart(
                                                productId = item.productId,
                                                quantity = item.quantity // Dodajemy quantity
                                            )
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Total: $total zł",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }

                is CartUiState.Error -> {
                    Text(
                        text = (state as CartUiState.Error).message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}