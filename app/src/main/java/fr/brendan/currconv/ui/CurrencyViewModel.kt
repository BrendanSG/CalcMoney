package fr.brendan.currconv.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.brendan.currconv.Currencies
import fr.brendan.currconv.CurrencyPreferences
import fr.brendan.currconv.CurrencyType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = CurrencyPreferences(application)

    val sourceCurrency: StateFlow<String> = preferences.sourceCurrencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Currencies.KRW.code
    )
    val destinationCurrency: StateFlow<String> = preferences.destinationCurrencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Currencies.EUR.code
    )

    fun updateCurrency(type: CurrencyType, code: String) {
        viewModelScope.launch {
            preferences.updateCurrency(type, code)
        }
    }
}