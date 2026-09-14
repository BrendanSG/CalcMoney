package fr.brendan.currconv.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import fr.brendan.currconv.ui.theme.LightGray
import fr.brendan.currconv.ui.theme.LightRed
import fr.brendan.currconv.ui.theme.Orange
import fr.brendan.currconv.ui.theme.Red
import fr.brendan.currconv.ui.theme.White

@Composable
fun CalculatorKeypad(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(),
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
                CalcButton(content = ButtonContent.Text("×"), type = ButtonType.Operator)
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
                containerColor = LightGray,
                contentColor = White,
                disabledContainerColor = LightRed,
                disabledContentColor = Red
            )
            is ButtonType.Action -> ButtonColors(
                containerColor = Orange,
                contentColor = White,
                disabledContainerColor = LightRed,
                disabledContentColor = Red
            )
            is ButtonType.Operator -> ButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = White,
                disabledContainerColor = LightRed,
                disabledContentColor = Red
            )
        },
        onClick = {
            TODO()
        }
    ) {
        when(content) {
            is ButtonContent.Text -> Text(
                text = content.value,
                style = MaterialTheme.typography.bodyLarge,
                color = White
            )
            is ButtonContent.Icon -> Icon(
                imageVector = content.imageVector,
                contentDescription = content.contentDescription
            )
        }
    }
}