package com.akzuza.finan.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.akzuza.finan.data.FinanTransactionEvent
import com.akzuza.finan.data.sms.CheckAndUpsertNewTransactions
import com.akzuza.finan.ui.components.FinanTransactionCard
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay


@Composable
fun FinanHomeScreen(
    homeState: FinanHomeState,
    onEvent: (FinanTransactionEvent) -> Unit
) {
    val lazyListState = LazyListState(
        firstVisibleItemIndex = 0
    )

    val transactionState by homeState.transactions.collectAsState(initial = emptyList())
    val contentResolver = LocalContext.current.contentResolver

    SwipeRefresh (
        state = rememberSwipeRefreshState(isRefreshing = homeState.refreshing),
        onRefresh = {
            onEvent(FinanTransactionEvent.StartRefreshTransactions)
            CheckAndUpsertNewTransactions(
                state = homeState,
                contentResolver,
                onEvent
            )
            onEvent(FinanTransactionEvent.StopRefreshTransactions)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
        ) {
            transactionState.forEach { transaction ->
                item {
                    FinanTransactionCard(finanTransaction = transaction)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewFinanHomeScreen() {
//    FinanHomeScreen(
//        homeState = FinanHomeState(transactions = LocalTransactionsList),
//        { transaction: Transaction? ->
//            if(transaction != null)
//                LocalTransactionsList.add(transaction)
//        }
//    )
}