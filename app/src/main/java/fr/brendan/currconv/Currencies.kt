package fr.brendan.currconv

data class Currency(
    val code: String,
    val symbol: String,
    val fullName: String,
    val flag: String?
)

object Currencies {
    val EUR = Currency("EUR", "€", "Euros", "\uD83C\uDDEA\uD83C\uDDFA")
    val KRW = Currency("KRW", "€", "Won Sud-Coréen", "\uD83C\uDDF0\uD83C\uDDF7")
    val USD = Currency("USD", "$", "Dollards Américains", "\uD83C\uDDFA\uD83C\uDDF8")

    fun getAll(): List<Currency> = listOf(EUR, KRW, USD)
    fun findByCode(code: String): Currency? = getAll().find { it.code.equals(code, ignoreCase = true) }
}

