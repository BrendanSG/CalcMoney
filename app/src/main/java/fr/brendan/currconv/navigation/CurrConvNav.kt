package fr.brendan.currconv.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import fr.brendan.currconv.CurrencyType
import fr.brendan.currconv.ui.screens.currencySelectorEntry
import fr.brendan.currconv.ui.screens.homeNavEntry

sealed interface CurrConvRoutes {
    data object Home: CurrConvRoutes
    data class CurrencySelector(
        val type: CurrencyType
    ): CurrConvRoutes
}

@Composable
fun CurrConvNav(
    modifier: Modifier = Modifier,
    defaultRoute: CurrConvRoutes = CurrConvRoutes.Home
) {

    val backStack = remember { mutableStateListOf(defaultRoute) }
    //val currentRoute = backStack.lastOrNull() ?: defaultRoute

    Scaffold { paddingValues ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            transitionSpec = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500)
                ) togetherWith slideOutVertically(
                    targetOffsetY = { 0 },
                    animationSpec = tween(500)
                )
            },
            popTransitionSpec = {
                slideInVertically(
                    initialOffsetY = { 0 },
                    animationSpec = tween(500)
                ) togetherWith slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(500)
                )
            },
            predictivePopTransitionSpec = {
                slideInVertically(
                    initialOffsetY = { 0 },
                    animationSpec = tween(500)
                ) togetherWith slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(500)
                )
            },
            entryProvider = { key ->
                when(key) {
                    is CurrConvRoutes.Home -> homeNavEntry(key, backStack)
                    is CurrConvRoutes.CurrencySelector -> currencySelectorEntry(key, backStack)
                }
            }
        )
    }
}