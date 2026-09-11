package fr.brendan.currconv.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import fr.brendan.currconv.Currencies
import fr.brendan.currconv.Currency
import fr.brendan.currconv.CurrencyType
import fr.brendan.currconv.navigation.CurrConvRoutes
import fr.brendan.currconv.ui.CurrencyViewModel
import fr.brendan.currconv.ui.theme.Green
import fr.brendan.currconv.ui.theme.LightGray
import fr.brendan.currconv.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun currencySelectorEntry(destination: CurrConvRoutes.CurrencySelector, backStack: SnapshotStateList<CurrConvRoutes>): NavEntry<CurrConvRoutes> = NavEntry(destination) {
    CurrencySelectorScreen(type = destination.type, backStack = backStack)
}

@Composable
private fun CurrencySelectorScreen(
    modifier: Modifier = Modifier,
    type: CurrencyType,
    backStack: SnapshotStateList<CurrConvRoutes>,
    viewModel: CurrencyViewModel = viewModel()
) {
    val sourceCurrencyCode by viewModel.sourceCurrency.collectAsState()
    val destinationCurrencyCode by viewModel.destinationCurrency.collectAsState()
    var selectedCurrencyCode by remember { mutableStateOf(if (type == CurrencyType.CurrencySource) sourceCurrencyCode else destinationCurrencyCode) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Changement de la devise ${if (type == CurrencyType.CurrencySource) "Source" else "de Destination"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(Currencies.getAll()) { currency ->
                    val isSelected = selectedCurrencyCode == currency.code
                    CurrencyButton(
                        currency = currency,
                        isSelected = isSelected,
                        onClick = {
                            selectedCurrencyCode = currency.code
                        }
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGray,
                    contentColor = White,
                ),
                onClick = {
                    backStack.removeLastOrNull()
                }
            ) {
                Text(
                    text = "Annuler",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(),
                onClick = {
                    val originalCurrencyCode = if (type == CurrencyType.CurrencySource) sourceCurrencyCode else destinationCurrencyCode
                    if (originalCurrencyCode != selectedCurrencyCode) {
                        viewModel.viewModelScope.launch {
                            viewModel.updateCurrency(type = type, code = selectedCurrencyCode)
                            delay(500)
                            viewModel.fetchRate(invalidate = true)
                        }
                    }
                    backStack.removeLastOrNull()
                }
            ) {
                Text(
                    text = "Valider",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CurrencyButton(
    modifier: Modifier = Modifier,
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(0.85f)
            .height(55.dp)
            .border(width = 2.dp, color = if (isSelected) Green else LightGray, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currency.fullName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currency.flag ?: "❓",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = currency.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}