package fr.brendan.currconv.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import fr.brendan.currconv.Currencies
import fr.brendan.currconv.CurrencyType
import fr.brendan.currconv.navigation.CurrConvRoutes
import fr.brendan.currconv.ui.CurrencyViewModel
import fr.brendan.currconv.ui.components.CalculatorKeypad
import fr.brendan.currconv.ui.components.CurrencyRow

fun homeNavEntry(destination: CurrConvRoutes.Home, backStack: SnapshotStateList<CurrConvRoutes>): NavEntry<CurrConvRoutes> = NavEntry(destination) {
    HomeScreen(backStack = backStack)
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    backStack: SnapshotStateList<CurrConvRoutes>,
    viewModel: CurrencyViewModel = viewModel()) {
    var sourceAmount by remember { mutableStateOf("0.0") }
    var convertedAmount by remember { mutableStateOf("0.0") }

    val sourceCurrencyCode by viewModel.sourceCurrency.collectAsState()
    val destinationCurrencyCode by viewModel.destinationCurrency.collectAsState()
    val rate by viewModel.cachedRate.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) { Text(
                        text = "Convertisseur de Devises",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrencyRow(
                        amount = sourceAmount,
                        currency = Currencies.findByCode(sourceCurrencyCode),
                        type = CurrencyType.CurrencySource,
                        backStack = backStack
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrencyRow(
                        amount = convertedAmount,
                        currency = Currencies.findByCode(destinationCurrencyCode),
                        type = CurrencyType.CurrencyDestination,
                        backStack = backStack
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            CalculatorKeypad()
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().clickable(enabled = true, onClick = {
                    viewModel.fetchRate(invalidate = true)
                }),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Taux actuel: $rate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Cliquez pour mettre à jour",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}