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
import fr.brendan.currconv.ui.components.ButtonAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    var operandA = MutableStateFlow("0")
    var operandB = MutableStateFlow("0")
    var typingState = MutableStateFlow(0) // 0=A, 1=B, 2=Clear on next type
    var typingDecimal = MutableStateFlow(false)
    var currentOperator = MutableStateFlow<ButtonAction.Operator?>(null)
    fun onKeypadAction(action: ButtonAction) {
        when (action) {
            // Numbers
            is ButtonAction.Number -> {
                val theOperand = if (typingState.value == 1) operandB else operandA
                theOperand.update { old ->
                    if (old == "0" || typingState.value == 2) {
                        if (typingState.value == 2) {
                            typingState.update { old ->
                                if (old == 2) 0 else 1
                            }
                        }
                        action.value.toString()
                    } else if (old.contains(".") && old.split(".")[1].length >= 2) {
                        old
                    } else {
                        old + action.value.toString()
                    }
                }
            }
            // Operators: Comma
            is ButtonAction.Operator.Comma -> {
                typingDecimal.update { true }

                val theOperand = if (typingState.value == 1) operandB else operandA
                theOperand.update { old ->
                    if (old.contains(".")) old else "$old."
                }
            }
            // Operators: All others operators
            is ButtonAction.Operator -> {
                if (currentOperator.value == action) {
                    operandB.update { "0" }
                    typingState.update { 2 }
                    currentOperator.update { null }
                } else {
                    typingState.update { 1 }
                    currentOperator.update { action }
                }
            }
            // Others
            is ButtonAction.Others.Clear -> {
                operandA.update { "0" }
                operandB.update { "0" }
                typingDecimal.update { false }
                typingState.update { 0 }
                currentOperator.update { null }
            }
            is ButtonAction.Others.Del -> {
                val theOperand = if (typingState.value == 1) operandB else operandA
                theOperand.update { old ->
                    if (old.length <= 1) {
                        typingDecimal.update { false }
                        "0"
                    } else {
                        val new = old.dropLast(1)
                        if (!new.contains(".")) {
                            typingDecimal.update { false }
                        }
                        new
                    }
                }
            }
            is ButtonAction.Others.Equals -> {
                val numA = operandA.value.toDoubleOrNull()
                val numB = operandB.value.toDoubleOrNull()
                if (numA == null || numB == null) return

                var result: Double
                when (currentOperator.value) {
                    ButtonAction.Operator.Add -> result = numA + numB
                    ButtonAction.Operator.Sub -> result = numA - numB
                    ButtonAction.Operator.Multiply -> result = numA * numB
                    ButtonAction.Operator.Divide -> {
                        if (numB == 0.0) throw IllegalArgumentException("Dividing by 0 is forbidden")
                        result = numA / numB
                    }
                    else -> return
                }

                Log.d("CurrencyViewModel", "Calculi result: $numA (${currentOperator.value}) $numB = $result")
                operandA.update { result.toString() }
                operandB.update { "0" }
                typingState.update { 2 }
                currentOperator.update { null }
            }
            is ButtonAction.Others.Swap -> TODO()
        }
    }
}

data class FrankfurterResponse(
    val amount: Float,
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)