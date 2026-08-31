package fr.brendan.currconv

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CurrencyPreferences(private val context: Context) {
    companion object {
        val SOURCE_CURRENCY_KEY = stringPreferencesKey("source_currency_key")
        val DESTINATION_CURRENCY_KEY = stringPreferencesKey("destination_currency_key")
    }

    val sourceCurrencyFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[SOURCE_CURRENCY_KEY] ?: Currencies.KRW.code }
    val destinationCurrencyFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[DESTINATION_CURRENCY_KEY] ?: Currencies.EUR.code }

    suspend fun updateCurrency(type: CurrencyType, code: String) {
        context.dataStore.edit { preferences ->
            preferences[when (type) {
                is CurrencyType.CurrencySource -> SOURCE_CURRENCY_KEY
                is CurrencyType.CurrencyDestination -> DESTINATION_CURRENCY_KEY
            }] = code
        }
    }
}
