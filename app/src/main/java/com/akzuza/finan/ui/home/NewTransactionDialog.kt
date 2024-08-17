package com.akzuza.finan.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.akzuza.finan.data.FinanTransactionEvent
import com.akzuza.finan.data.TransactionType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewTransactionDialog(state: FinanHomeState, onEvent: (FinanTransactionEvent) -> Unit) {

    onEvent(FinanTransactionEvent.SetType(TransactionType.CASH))

    Dialog(
        onDismissRequest = { onEvent(FinanTransactionEvent.HideDialog) }
    ) {

        OutlinedCard {

            Column (
                modifier = Modifier
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    text = "Add Cash Transaction",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = if(state.amount == 0) "" else "${state.amount}",
                    onValueChange = { it ->
                        if(it.isNotBlank() && it.isDigitsOnly())
                            onEvent(FinanTransactionEvent.SetAmount(it.toInt()))
                    },
                    label = { Text("Amount (INR)") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = state.from,
                    onValueChange = { it -> onEvent(FinanTransactionEvent.SetFrom(it)) },
                    label = { Text("From") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = state.to,
                    onValueChange = { it -> onEvent(FinanTransactionEvent.SetTo(it)) },
                    label = { Text("To") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                BottomBarButtons(onEvent = onEvent)
            }

        }

    }
}

@Composable
fun BottomBarButtons(onEvent: (FinanTransactionEvent) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val modifier: Modifier = Modifier.weight(1F)
        TextButton(onClick = { onEvent(FinanTransactionEvent.HideDialog) }, modifier = modifier) {
            Text(text = "Cancel")
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        TextButton(onClick = {
            onEvent(FinanTransactionEvent.SetDateTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ))
            onEvent(FinanTransactionEvent.AddTransaction)
            onEvent(FinanTransactionEvent.HideDialog)}, modifier = modifier) {
            Text(text = "Add")
        }
    }
}