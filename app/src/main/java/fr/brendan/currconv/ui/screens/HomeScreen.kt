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
    val sourceCurrencyCode by viewModel.sourceCurrency.collectAsState()
    val destinationCurrencyCode by viewModel.destinationCurrency.collectAsState()
    val rate by viewModel.cachedRate.collectAsState()

    val operandA by viewModel.operandA.collectAsState()
    val operandB by viewModel.operandB.collectAsState()
    val typingState by viewModel.typingState.collectAsState()
    val typingOperand = if (typingState == 1) operandB else operandA

    val sourceAmountToShow =
        if (typingOperand.contains("."))
            "%,.2f".format(typingOperand.toDoubleOrNull() ?: 0.0)
        else
            "%,d".format(typingOperand.toIntOrNull() ?: 0)
    val destAmount = (operandA.toDoubleOrNull() ?: 0.0) * (rate?.rate ?: 0).toDouble()

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
                        amount = sourceAmountToShow,
                        currency = Currencies.findByCode(sourceCurrencyCode),
                        type = CurrencyType.CurrencySource,
                        backStack = backStack
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrencyRow(
                        amount = if (typingState == 1) "?" else "%,.2f".format(destAmount),
                        currency = Currencies.findByCode(destinationCurrencyCode),
                        type = CurrencyType.CurrencyDestination,
                        backStack = backStack
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            CalculatorKeypad(onAction = { action ->
                viewModel.onKeypadAction(action)
            })
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