package fr.brendan.currconv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import fr.brendan.currconv.Currencies
import fr.brendan.currconv.Currency
import fr.brendan.currconv.navigation.CurrConvRoutes

fun homeNavEntry(destination: CurrConvRoutes.Home): NavEntry<CurrConvRoutes> = NavEntry(destination) {
    HomeScreen()
}

@Composable
@Preview(showBackground = true, device = "id:pixel_7", showSystemUi = true)
private fun HomeScreen(modifier: Modifier = Modifier) {
    var sourceAmount by remember { mutableStateOf("0.0") }
    var convertedAmount by remember { mutableStateOf("0.0") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) { Text(
                        text = "Convertisseur de Devises",
                        fontSize = 22.sp,
                        color = Color(0xFF363636)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrencyRow(
                        amount = sourceAmount,
                        currency = Currencies.KRW
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrencyRow(
                        amount = convertedAmount,
                        currency = Currencies.EUR
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CalcButton(content = ButtonContent.Text("C"), type = ButtonType.Action)
                        CalcButton(content = ButtonContent.Icon(Icons.AutoMirrored.Filled.Backspace, "Del"), type = ButtonType.Action)
                        CalcButton(content = ButtonContent.Icon(Icons.Filled.SwapVert, "Swap"), type = ButtonType.Action)
                        CalcButton(content = ButtonContent.Text("÷"), type = ButtonType.Operator)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CalcButton(content = ButtonContent.Text("1"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("2"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("3"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("x"), type = ButtonType.Operator)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CalcButton(content = ButtonContent.Text("4"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("5"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("6"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("+"), type = ButtonType.Operator)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CalcButton(content = ButtonContent.Text("7"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("8"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("9"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("-"), type = ButtonType.Operator)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CalcButton(content = ButtonContent.Text("C"), type = ButtonType.Action)
                        CalcButton(content = ButtonContent.Text("0"), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text(","), type = ButtonType.Number)
                        CalcButton(content = ButtonContent.Text("="), type = ButtonType.Action)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Taux actuel: 1€ = 1600₩ (30/08/2026 20:03)",
                    fontSize = 15.sp,
                    color = Color(0xFF363636),
                )
                Text(
                    text = "Cliquez pour mettre à jour",
                    fontSize = 15.sp,
                    color = Color(0xFF363636),
                )
            }
        }
    }
}

@Composable
private fun CurrencyRow(amount: String, currency: Currency) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable(enabled = true, onClick = {
                TODO()
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = amount,
            color = Color(0xFF363636),
            fontSize = 22.sp
        )

        Column(
            modifier = Modifier.fillMaxHeight().clickable(enabled = true, onClick = {
                TODO()
            }),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currency.flag ?: "❓",
                color = Color(0xFF363636),
                fontSize = 22.sp
            )
            Text(
                text = currency.code,
                color = Color(0xFF363636),
            )
        }
    }
}

sealed interface ButtonContent {
    data class Text(val value: String): ButtonContent
    data class Icon(val imageVector: ImageVector, val contentDescription: String): ButtonContent
}

sealed interface ButtonType {
    data object Number: ButtonType
    data object Action: ButtonType
    data object Operator: ButtonType
}

@Composable
private fun CalcButton(
    content: ButtonContent,
    type: ButtonType
) {
    Button(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        colors = when(type) {
            is ButtonType.Number -> ButtonColors(
                containerColor = Color(0xFF919191),
                contentColor = Color(0xFFFFFFFF),
                disabledContainerColor = Color(0xFFF3635A),
                disabledContentColor = Color(0xFFFF0000)
            )
            is ButtonType.Action -> ButtonColors(
                containerColor = Color(0xFFFF9800),
                contentColor = Color(0xFFFFFFFF),
                disabledContainerColor = Color(0xFFF3635A),
                disabledContentColor = Color(0xFFFF0000)
            )
            is ButtonType.Operator -> ButtonColors(
                containerColor = Color(0xFF545454),
                contentColor = Color(0xFFFFFFFF),
                disabledContainerColor = Color(0xFFF3635A),
                disabledContentColor = Color(0xFFFF0000)
            )
        },
        onClick = {
            TODO()
        }
    ) {
        when(content) {
            is ButtonContent.Text -> Text(
                text = content.value,
                fontSize = 22.sp
            )
            is ButtonContent.Icon -> Icon(
                imageVector = content.imageVector,
                contentDescription = content.contentDescription
            )
        }
    }
}