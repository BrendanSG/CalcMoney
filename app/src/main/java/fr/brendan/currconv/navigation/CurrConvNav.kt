package fr.brendan.currconv.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import fr.brendan.currconv.ui.screens.homeNavEntry

sealed interface CurrConvRoutes {
    data object Home: CurrConvRoutes
}

@Composable
fun CurrConvNav(
    defaultRoute: CurrConvRoutes = CurrConvRoutes.Home,
    modifier: Modifier = Modifier) {

    val backStack = remember { mutableStateListOf<CurrConvRoutes>(defaultRoute) }
    val currentRoute = backStack.lastOrNull() ?: defaultRoute

    Scaffold() { paddingValues ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when(key) {
                    is CurrConvRoutes.Home -> homeNavEntry(key)
                }
            }
        )
    }
}