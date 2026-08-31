package fr.brendan.currconv

sealed interface CurrencyType {
    data object CurrencySource : CurrencyType
    data object CurrencyDestination : CurrencyType
}

data class Currency(
    val code: String,
    val symbol: String,
    val fullName: String,
    val flag: String?
)

object Currencies {
    val EUR = Currency("EUR", "€", "Euros", "\uD83C\uDDEA\uD83C\uDDFA")
    val KRW = Currency("KRW", "₩", "Won Sud-Coréen", "\uD83C\uDDF0\uD83C\uDDF7")
    val USD = Currency("USD", "$", "Dollards Américains", "\uD83C\uDDFA\uD83C\uDDF8")
    val GBP = Currency("GBP", "£", "Livres Sterling", "\uD83C\uDDEC\uD83C\uDDE7")
    val JPY = Currency("JPY", "¥", "Yens Japonais", "\uD83C\uDDEF\uD83C\uDDF5")
    val CAD = Currency("CAD", "$", "Dollars Canadiens", "\uD83C\uDDE8\uD83C\uDDE6")
    val AUD = Currency("AUD", "$", "Dollars Australiens", "\uD83C\uDDE6\uD83C\uDDFA")
    val CHF = Currency("CHF", "CHF", "Francs Suisses", "\uD83C\uDDE8\uD83C\uDDED")
    val CNY = Currency("CNY", "¥", "Yuans Chinois", "\uD83C\uDDE8\uD83C\uDDF3")
    val INR = Currency("INR", "₹", "Roupies Indiennes", "\uD83C\uDDEE\uD83C\uDDF3")
    val BRL = Currency("BRL", "R$", "Reais Brésiliens", "\uD83C\uDDE7\uD83C\uDDF7")
    val MXN = Currency("MXN", "$", "Pesos Mexicains", "\uD83C\uDDF2\uD83C\uDDFD")

    fun getAll(): List<Currency> = listOf(EUR, KRW, USD, GBP, JPY, CAD, AUD, CHF, CNY, INR, BRL, MXN)
    fun findByCode(code: String): Currency? = getAll().find { it.code.equals(code, ignoreCase = true) }
}

