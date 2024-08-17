package com.akzuza.finan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akzuza.finan.data.FinanTransaction
import com.akzuza.finan.data.TransactionType
import java.time.format.DateTimeFormatter

@Composable
fun FinanTransactionCard(finanTransaction: FinanTransaction) {
    val cardColor = CardColors(
        contentColor = if (finanTransaction.type == TransactionType.UPI)
            MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.onSecondary ,
        containerColor = if (finanTransaction.type == TransactionType.UPI)
            MaterialTheme.colorScheme.secondaryContainer
        else Color(0, 190, 90),
        disabledContainerColor = Color.Unspecified,
        disabledContentColor = Color.Unspecified
    )

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        colors = cardColor
    ){
        Column (
            modifier = Modifier.padding(10.dp)
        ) {

            Row {
                val modifier = Modifier
                    .weight(1F)
                    .wrapContentHeight()
                AccountView(finanTransaction.from, modifier)
                AccountView(finanTransaction.to, modifier)
            }

            Text(
                "Amount: INR ${finanTransaction.amount}",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )

            Text(
                "Date: ${finanTransaction.datetime.format(formatter)}",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }
    }
}

@Composable
fun AccountView(account: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(45.dp)
        )

        Text(
            text = account,
            fontSize = 20.sp
        )
    }
}

@Preview
@Composable
fun PreviewAccountView(){
    AccountView("Person 1")
}

@Preview
@Composable
fun PreviewFinanTransactionCard() {

}