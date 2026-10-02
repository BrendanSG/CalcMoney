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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.brendan.currconv.ui.CurrencyViewModel
import fr.brendan.currconv.ui.theme.LightGray
import fr.brendan.currconv.ui.theme.LightOrange
import fr.brendan.currconv.ui.theme.LightRed
import fr.brendan.currconv.ui.theme.Orange
import fr.brendan.currconv.ui.theme.White

@Composable
fun CalculatorKeypad(
    modifier: Modifier = Modifier,
    onAction: (ButtonAction) -> Unit,
    viewModel: CurrencyViewModel = viewModel()
) {
    val currentOperator by viewModel.currentOperator.collectAsState()
    val operandB by viewModel.operandB.collectAsState()

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
                CalcButton(action = ButtonAction.Others.Clear, onAction = onAction)
                CalcButton(action = ButtonAction.Others.Del, onAction = onAction)
                CalcButton(action = ButtonAction.Others.Swap, onAction = onAction)
                CalcButton(action = ButtonAction.Operator.Divide, onAction = onAction, selected = currentOperator == ButtonAction.Operator.Divide)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalcButton(action = ButtonAction.Number(1), onAction = onAction)
                CalcButton(action = ButtonAction.Number(2), onAction = onAction)
                CalcButton(action = ButtonAction.Number(3), onAction = onAction)
                CalcButton(action = ButtonAction.Operator.Multiply, onAction = onAction, selected = currentOperator == ButtonAction.Operator.Multiply)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalcButton(action = ButtonAction.Number(4), onAction = onAction)
                CalcButton(action = ButtonAction.Number(5), onAction = onAction)
                CalcButton(action = ButtonAction.Number(6), onAction = onAction)
                CalcButton(action = ButtonAction.Operator.Add, onAction = onAction, selected = currentOperator == ButtonAction.Operator.Add)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalcButton(action = ButtonAction.Number(7), onAction = onAction)
                CalcButton(action = ButtonAction.Number(8), onAction = onAction)
                CalcButton(action = ButtonAction.Number(9), onAction = onAction)
                CalcButton(action = ButtonAction.Operator.Sub, onAction = onAction, selected = currentOperator == ButtonAction.Operator.Sub)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalcButton(action = ButtonAction.Others.Clear, onAction = onAction)
                CalcButton(action = ButtonAction.Number(0), onAction = onAction)
                CalcButton(action = ButtonAction.Operator.Comma, onAction = onAction)
                CalcButton(action = ButtonAction.Others.Equals, onAction = onAction, enabled = (currentOperator != ButtonAction.Operator.Divide || operandB.toDoubleOrNull() != 0.0))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

sealed class ButtonAction {
    // 0 1 2 3 4 5 6 7 8 9
    data class Number(val value: Int) : ButtonAction()

    // + - × ÷
    sealed class Operator : ButtonAction() {
        object Add : Operator()
        object Sub : Operator()
        object Multiply : Operator()
        object Divide : Operator()
        object Comma : Operator()

        override fun toString(): String {
            return this.javaClass.simpleName
        }
    }

    // Clear, Del, Swap, Equals
    sealed class Others : ButtonAction() {
        object Clear : Others()
        object Del : Others()
        object Equals : Others()
        object Swap : Others()
    }
}

@Composable
private fun CalcButton(
    action: ButtonAction,
    enabled: Boolean = true,
    onAction: (ButtonAction) -> Unit,
    selected: Boolean = false
) {

    Button(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        colors = when(action) {
            is ButtonAction.Number -> ButtonColors(
                containerColor = LightGray,
                contentColor = White,
                disabledContainerColor = LightRed,
                disabledContentColor = White
            )
            is ButtonAction.Operator -> ButtonColors(
                containerColor = if (selected) LightOrange else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = White,
                disabledContainerColor = LightRed,
                disabledContentColor = White
            )
            is ButtonAction.Others -> ButtonColors(
                containerColor = Orange,
                contentColor = White,
                disabledContainerColor = LightRed,
                disabledContentColor = White
            )
        },
        onClick = {
            if (!enabled) return@Button
            onAction(action)
        },
        enabled = enabled
    ) {
        when(action) {
            // Numbers
            is ButtonAction.Number -> Text(
                text = action.value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = White
            )
            // Operators
            is ButtonAction.Operator -> Text(
                text = when(action) {
                    is ButtonAction.Operator.Add -> "+"
                    is ButtonAction.Operator.Sub -> "-"
                    is ButtonAction.Operator.Multiply -> "×"
                    is ButtonAction.Operator.Divide -> "÷"
                    is ButtonAction.Operator.Comma -> ","
                },
                style = MaterialTheme.typography.bodyLarge,
                color = White
            )
            // Others
            is ButtonAction.Others.Clear -> Text(
                text = "C",
                style = MaterialTheme.typography.bodyLarge,
                color = White
            )
            is ButtonAction.Others.Del -> Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Del"
            )
            is ButtonAction.Others.Swap -> Icon(
                imageVector = Icons.Filled.SwapVert,
                contentDescription = "Swap"
            )
            is ButtonAction.Others.Equals -> Text(
                text = "=",
                style = MaterialTheme.typography.bodyLarge,
                color = White
            )
        }
    }
}