package fr.brendan.currconv

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class CurrencyRate(
    val rate: Float,
    val lastUpdated: Long,
    val hash: String,

    val sourceSymbol: String,
    val destinationSymbol: String
) {
    override fun toString(): String {
        val instant = Instant.ofEpochMilli(lastUpdated*1000)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault())

        if (rate > 1) {
            return "1${sourceSymbol} = ${"%.2f".format(rate)}$destinationSymbol (${formatter.format(instant)})"
        } else {
            return "${"%.2f".format(1/rate)}${sourceSymbol} = 1$destinationSymbol (${formatter.format(instant)})"
        }
    }
}

class CurrencyPreferences(private val context: Context) {
    companion object {
        val SOURCE_CURRENCY_KEY = stringPreferencesKey("source_currency_key")
        val DESTINATION_CURRENCY_KEY = stringPreferencesKey("destination_currency_key")

        val RATE_VALUE_KEY = floatPreferencesKey("rate_value_key")
        val RATE_LAST_UPDATED_KEY = longPreferencesKey("rate_last_updated_key")
        val RATE_HASH_KEY = stringPreferencesKey("rate_hash_key")
        val RATE_SOURCE_KEY = stringPreferencesKey("rate_source_key")
        val RATE_DESTINATION_KEY = stringPreferencesKey("rate_destination_key")
    }

    val sourceCurrencyFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[SOURCE_CURRENCY_KEY] ?: Currencies.KRW.code }
    val destinationCurrencyFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[DESTINATION_CURRENCY_KEY] ?: Currencies.EUR.code }

    val rateFlow: Flow<CurrencyRate?> = context.dataStore.data.map { preferences ->
        val rate = preferences[RATE_VALUE_KEY]
        val lastUpdated = preferences[RATE_LAST_UPDATED_KEY]
        val hash = preferences[RATE_HASH_KEY]
        val source = preferences[RATE_SOURCE_KEY]
        val destination = preferences[RATE_DESTINATION_KEY]

        if (rate == null || lastUpdated == null || hash == null || source == null || destination == null) {
            null
        } else {
            CurrencyRate(rate, lastUpdated, hash, source, destination)
        }
    }

    suspend fun updateCurrency(type: CurrencyType, code: String) {
        context.dataStore.edit { preferences ->
            preferences[when (type) {
                is CurrencyType.CurrencySource -> SOURCE_CURRENCY_KEY
                is CurrencyType.CurrencyDestination -> DESTINATION_CURRENCY_KEY
            }] = code
        }
    }

    suspend fun updateRate(rate: CurrencyRate) {
        context.dataStore.edit { preferences ->
            preferences[RATE_VALUE_KEY] = rate.rate
            preferences[RATE_LAST_UPDATED_KEY] = rate.lastUpdated
            preferences[RATE_HASH_KEY] = rate.hash
            preferences[RATE_SOURCE_KEY] = rate.sourceSymbol
            preferences[RATE_DESTINATION_KEY] = rate.destinationSymbol
        }
    }
}
