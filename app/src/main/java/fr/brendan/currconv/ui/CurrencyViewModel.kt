package fr.brendan.currconv.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import fr.brendan.currconv.Currencies
import fr.brendan.currconv.CurrencyPreferences
import fr.brendan.currconv.CurrencyRate
import fr.brendan.currconv.CurrencyType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = CurrencyPreferences(application)

    val sourceCurrency: StateFlow<String> = preferences.sourceCurrencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "?"
    )
    val destinationCurrency: StateFlow<String> = preferences.destinationCurrencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "?"
    )

    val cachedRate: StateFlow<CurrencyRate?> = preferences.rateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            combine(sourceCurrency, destinationCurrency) { source, destination ->
                Pair(source, destination)
            }
            .filter { (source, destination) ->
                source.isNotBlank() && source != "?" && destination.isNotBlank() && destination != "?"
            }
            .distinctUntilChanged()
            .collect { (source, destination) ->
                fetchRate()
            }
        }
    }

    fun updateCurrency(type: CurrencyType, code: String) {
        viewModelScope.launch {
            preferences.updateCurrency(type, code)
        }
    }

    fun fetchRate(invalidate : Boolean = false) {
        viewModelScope.launch {
            val currentRate = cachedRate.value
            val isExpired = currentRate == null ||
                    (System.currentTimeMillis()/1000 - currentRate.lastUpdated)>12*3600 ||
                    currentRate.hash != "${sourceCurrency.value}_${destinationCurrency.value}"

            Log.d("CurrentViewModel", "fetchRate() > isExpired = $isExpired")

            if (isExpired || invalidate) {
                try {
                    val res = withContext(Dispatchers.IO) {
                        httpGET("https://api.frankfurter.dev/v1/latest?from=${sourceCurrency.value}&to=${destinationCurrency.value}")
                    }

                    Log.d("CurrencyViewModel", res)
                    val gson = Gson()
                    val resObj = gson.fromJson(res, FrankfurterResponse::class.java)
                    preferences.updateRate(CurrencyRate(
                        rate = resObj.rates[destinationCurrency.value] ?: 0f,
                        lastUpdated = System.currentTimeMillis()/1000,
                        hash = "${sourceCurrency.value}_${destinationCurrency.value}",
                        sourceSymbol = Currencies.findByCode(sourceCurrency.value)?.symbol ?: "?",
                        destinationSymbol = Currencies.findByCode(destinationCurrency.value)?.symbol ?: "?"
                    ))
                } catch (e: Exception) {
                    Log.e("CurrencyViewModel", "Erreur lors de la requête du taux")
                    e.message?.let { Log.e("CurrencyViewModel", it) }
                }
            }
        }
    }

    private fun httpGET(urlStr: String) : String {
        Log.d("CurrencyViewModel", "GET $urlStr")
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpsURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val resCode = connection.responseCode
            if (resCode == HttpsURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
                reader.close()
                return stringBuilder.toString()
            } else {
                throw Exception("Erreur HTTP: $resCode")
            }
        } finally {
            connection.disconnect()
        }
    }
}

data class FrankfurterResponse(
    val amount: Float,
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)